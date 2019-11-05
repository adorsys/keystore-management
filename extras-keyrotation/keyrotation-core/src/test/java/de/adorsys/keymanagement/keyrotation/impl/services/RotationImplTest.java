package de.adorsys.keymanagement.keyrotation.impl.services;

import com.google.common.collect.ImmutableMap;
import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.core.metadata.WithPersister;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import de.adorsys.keymanagement.keyrotation.BaseMockitoTest;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStoreAccess;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import de.adorsys.keymanagement.keyrotation.api.services.KeyGenerator;
import de.adorsys.keymanagement.keyrotation.api.types.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.security.KeyStore;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.keyrotation.api.types.KeyState.STATUS;
import static de.adorsys.keymanagement.keyrotation.api.types.KeyState.TYPE;
import static de.adorsys.keymanagement.keyrotation.api.types.KeyStatus.*;
import static de.adorsys.keymanagement.keyrotation.api.types.KeyType.ENCRYPTING;
import static de.adorsys.keymanagement.keyrotation.api.types.KeyType.SECRET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RotationImplTest extends BaseMockitoTest {

    private static final Instant BASE_TIME = Instant.parse("2019-01-01T00:00:00Z");

    private static final Duration VALIDITY_DT = Duration.ofMinutes(10);
    private static final Duration LEGACY_DT = Duration.ofMinutes(30);
    private static final int NUM_VALID = 3;
    private static final Supplier<char[]> KEY_PASSWORD = "PASSWD"::toCharArray;
    private static final Supplier<char[]> KEYSTORE_PASSWORD = "STORE-PASSWD"::toCharArray;

    @Spy
    private KeyRotationConfig config = RotationConfig.builder()
            .keyPassword(KEY_PASSWORD)
            .keyStorePassword(KEYSTORE_PASSWORD)
            .validity(VALIDITY_DT)
            .legacy(LEGACY_DT)
            .countValidByType(
                    ImmutableMap.of(
                            SECRET, NUM_VALID,
                            KeyType.ENCRYPTING, NUM_VALID
                    )
            )
            .build();

    @Mock
    private RotationLocker locker;

    @Mock
    private Clock timeSource;

    @Mock
    private KeyStoreAccess access;

    @Captor
    private ArgumentCaptor<KeyStore> persistCaptor;

    private Juggler juggler = DaggerBCJuggler.builder()
            .keyStoreConfig(KeyStoreConfig.builder().type("UBER").build())
            .metadataConfig(MetadataPersistenceConfig.builder().metadataClass(KeyState.class).build())
            .metadataPersister(new WithPersister())
            .build();
    private KeyGenerator generator;
    private RotationImpl rotation;

    @BeforeEach
    void init() {
        when(timeSource.instant()).thenReturn(BASE_TIME);
        doAnswer(inv -> {
            ((Runnable) inv.getArgument(0)).run();
            return null;
        }).when(locker).executeWithLock(any(Runnable.class));

        generator = new KeyGeneratorImpl(juggler, config);
        rotation = new RotationImpl(generator, config, juggler, timeSource, access, locker);
        doAnswer(inv -> null).when(access).write(persistCaptor.capture());
    }

    @AfterEach
    void validateLockUsage() {
        verify(locker, atLeastOnce()).executeWithLock(any(Runnable.class));
    }

    @Test
    void testEmptyKeystoreIsFilledWithValid() {
        rotation.rotate();

        assertThat(entries().all()).hasSize(2 * NUM_VALID);
        assertThat(entries().privateKeys().toCollection()).hasSize(NUM_VALID);
        assertThat(entries().secretKeys().toCollection()).hasSize(NUM_VALID);

        assertThat(state(SECRET)).extracting(KeyState::getStatus).containsOnly(VALID);
        assertThat(state(SECRET)).extracting(KeyState::getNotAfter).containsOnly(BASE_TIME.plus(VALIDITY_DT));
        assertThat(state(SECRET)).extracting(KeyState::getExpireAt).containsOnly(BASE_TIME.plus(LEGACY_DT));
        assertThat(state(ENCRYPTING)).extracting(KeyState::getStatus).containsOnly(VALID);
        assertThat(state(ENCRYPTING)).extracting(KeyState::getNotAfter).containsOnly(BASE_TIME.plus(VALIDITY_DT));
        assertThat(state(ENCRYPTING)).extracting(KeyState::getExpireAt).containsOnly(BASE_TIME.plus(LEGACY_DT));
    }

    @Test
    void testValidAreSentToLegacy() {
        // Fills the keystore
        rotation.rotate();
        when(access.read()).thenReturn(persistCaptor.getValue());
        Instant newBaseTime = BASE_TIME.plus(VALIDITY_DT).plusSeconds(1);
        when(timeSource.instant()).thenReturn(newBaseTime);

        rotation.rotate();

        assertThat(entries().all()).hasSize(4 * NUM_VALID);
        assertThat(entries().privateKeys().toCollection()).hasSize(2 * NUM_VALID);
        assertThat(entries().secretKeys().toCollection()).hasSize(2 * NUM_VALID);
        assertThat(state(SECRET, VALID)).hasSize(NUM_VALID);
        assertThat(state(ENCRYPTING, VALID)).hasSize(NUM_VALID);
        assertThat(state(SECRET, LEGACY)).hasSize(NUM_VALID);
        assertThat(state(ENCRYPTING, LEGACY)).hasSize(NUM_VALID);
        assertThat(state(SECRET, LEGACY)).extracting(KeyState::getNotAfter).containsOnly(BASE_TIME.plus(VALIDITY_DT));
        assertThat(state(ENCRYPTING, LEGACY)).extracting(KeyState::getExpireAt).containsOnly(BASE_TIME.plus(LEGACY_DT));
        assertThat(state(SECRET, VALID)).extracting(KeyState::getNotAfter).containsOnly(newBaseTime.plus(VALIDITY_DT));
        assertThat(state(ENCRYPTING, VALID)).extracting(KeyState::getExpireAt).containsOnly(newBaseTime.plus(LEGACY_DT));
    }

    @Test
    void testLegacyAreSentToExpiredAndRemoved() {
        // Fills the keystore
        rotation.rotate();
        Set<String> expiredIds = entries().all().stream().map(KeyAlias::getAlias).collect(Collectors.toSet());
        when(access.read()).thenReturn(getLastPersistedKeystore());
        Instant newBaseTime = BASE_TIME.plus(LEGACY_DT).plusSeconds(1);
        when(timeSource.instant()).thenReturn(newBaseTime);

        rotation.rotate();

        assertThat(entries().all()).hasSize(2 * NUM_VALID);
        assertThat(entries().all().stream().map(KeyAlias::getAlias).collect(Collectors.toSet()))
                .doesNotContainAnyElementsOf(expiredIds);
        assertThat(entries().privateKeys().toCollection()).hasSize(NUM_VALID);
        assertThat(entries().secretKeys().toCollection()).hasSize(NUM_VALID);
        assertThat(state(SECRET, VALID)).hasSize(NUM_VALID);
        assertThat(state(ENCRYPTING, VALID)).hasSize(NUM_VALID);
        assertThat(state(SECRET, LEGACY)).hasSize(0);
        assertThat(state(ENCRYPTING, LEGACY)).hasSize(0);
        assertThat(state(SECRET, EXPIRED)).hasSize(0);
        assertThat(state(ENCRYPTING, EXPIRED)).hasSize(0);
    }

    @Test
    void testFullRotation() {
        // Fills the keystore
        rotation.rotate();
        Set<String> expiredIds = entries().all().stream().map(KeyAlias::getAlias).collect(Collectors.toSet());
        assertThat(expiredIds).hasSize(2 * NUM_VALID);
        when(timeSource.instant()).thenReturn(BASE_TIME.plus(VALIDITY_DT).plusSeconds(1));

        when(access.read()).thenReturn(getLastPersistedKeystore());
        rotation.rotate();

        Set<String> legacyIds = entries().all().stream()
                .map(KeyAlias::getAlias)
                .filter(it -> !expiredIds.contains(it))
                .collect(Collectors.toSet());
        assertThat(legacyIds).hasSize(2 * NUM_VALID);

        when(timeSource.instant()).thenReturn(BASE_TIME.plus(LEGACY_DT).plusSeconds(1));
        when(access.read()).thenReturn(getLastPersistedKeystore());
        rotation.rotate();

        assertThat(entries().all()).hasSize(4 * NUM_VALID); // VALID + LEGACY
        assertThat(entries().privateKeys().toCollection()).hasSize(2 * NUM_VALID);
        assertThat(entries().secretKeys().toCollection()).hasSize(2 * NUM_VALID);
        assertThat(state(SECRET, VALID)).hasSize(NUM_VALID);
        assertThat(state(ENCRYPTING, VALID)).hasSize(NUM_VALID);
        assertThat(state(SECRET, LEGACY)).hasSize(NUM_VALID);
        assertThat(state(ENCRYPTING, LEGACY)).hasSize(NUM_VALID);
        assertThat(state(SECRET, EXPIRED)).hasSize(0);
        assertThat(state(ENCRYPTING, EXPIRED)).hasSize(0);

        assertThat(entries().all().stream().map(KeyAlias::getAlias).collect(Collectors.toSet()))
                .doesNotContainAnyElementsOf(expiredIds);
        assertThat(entries().all().stream().map(KeyAlias::getAlias).collect(Collectors.toSet()))
                .containsAll(legacyIds);
    }

    private KeyStore getLastPersistedKeystore() {
        return persistCaptor.getAllValues().get(persistCaptor.getAllValues().size() - 1);
    }

    private EntryView<Query<KeyEntry>> entries() {
        KeyStore ks = getLastPersistedKeystore();
        return juggler.readKeys().fromKeyStore(ks, id -> KEY_PASSWORD.get()).entries();
    }

    private Set<KeyState> state(KeyType type) {
        return entries().retrieve(equal(TYPE, type)).toCollection().stream()
                .map(it -> it.getMeta(KeyState.class))
                .collect(Collectors.toSet());
    }

    private Set<KeyState> state(KeyType type, KeyStatus status) {
        return entries().retrieve(and(equal(TYPE, type), equal(STATUS, status))).toCollection().stream()
                .map(it -> it.getMeta(KeyState.class))
                .collect(Collectors.toSet());
    }
}