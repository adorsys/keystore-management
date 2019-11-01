package de.adorsys.keymanagement.keyrotation.impl.services;

import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.AliasWithMeta;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.keyrotation.api.services.KeyGenerator;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.services.Rotation;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.api.types.KeyState;
import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.security.KeyStore;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static de.adorsys.keymanagement.keyrotation.api.types.KeyState.*;

@Slf4j
public class RotationImpl implements Rotation {

    private final KeyGenerator generator;
    private final KeyRotationConfig config;
    private final Juggler juggler;
    private final Clock timeSource;
    private final KeyStorePersistence persistence;
    private final RotationLocker locker;

    @Inject
    public RotationImpl(KeyGenerator generator, KeyRotationConfig config,
                        Juggler juggler, @Nullable Clock timeSource, KeyStorePersistence persistence,
                        RotationLocker locker) {
        this.generator = generator;
        this.config = config;
        this.juggler = juggler;
        this.timeSource = null == timeSource ? Clock.systemUTC() : timeSource;
        this.persistence = persistence;
        this.locker = locker;
    }

    @Override
    public void rotate() {
        locker.executeWithLock(this::doRotate);
    }

    private void doRotate() {
        Instant now = timeSource.instant();
        KeyStore ks = readOrCreateKeystoreIfMissing();
        EntryView<Query<KeyEntry>> keys = juggler.readKeys().fromKeyStore(
                ks,
                id -> config.getKeyPassword().get()
        ).entries();

        moveValidToLegacy(now, keys);
        moveLegacyToExpired(now, keys);
        dropExpired(keys);
        ensureThereAreEnoughValidKeys(now, keys);
    }

    private void moveValidToLegacy(Instant now, EntryView<Query<KeyEntry>> keys) {
        ResultCollection<KeyEntry> legacy = keys.retrieve(
                and(in(TYPE, config.getEnabledFor()), BECAME_LEGACY.apply(now))
        ).toCollection();
        keys.update(legacy.stream().map(it -> toStatus(it, KeyStatus.LEGACY)).collect(Collectors.toList()));
        log.info("Moved valid to legacy ids: {}", nameAndType(legacy));
    }

    private void moveLegacyToExpired(Instant now, EntryView<Query<KeyEntry>> keys) {
        ResultCollection<KeyEntry> expired = keys.retrieve(
                and(in(TYPE, config.getEnabledFor()), BECAME_EXPIRED.apply(now))
        ).toCollection();
        keys.update(expired.stream().map(it -> toStatus(it, KeyStatus.EXPIRED)).collect(Collectors.toList()));
        log.info("Moved legacy to expired ids: {}", nameAndType(expired));
    }

    private void dropExpired(EntryView<Query<KeyEntry>> keys) {
        ResultCollection<KeyEntry> expired = keys.retrieve(
                and(in(TYPE, config.getEnabledFor()), equal(STATUS, KeyStatus.EXPIRED))
        ).toCollection();
        keys.remove(expired);
        log.info("Removed expired ids: {}", nameAndType(expired));
    }

    private void ensureThereAreEnoughValidKeys(Instant now, EntryView<Query<KeyEntry>> keys) {
        for (KeyType forType : config.getEnabledFor()) {
            int countValidForType = keys.retrieve(
                    and(equal(TYPE, forType), equal(STATUS, KeyStatus.VALID))
            ).toCollection().size();

            int missing = countValidForType - config.getKeysByType().get(forType);
            generateKeysIfNeeded(now, keys, forType, missing);
        }
    }

    private void generateKeysIfNeeded(Instant now, EntryView<Query<KeyEntry>> keys, KeyType forType, int missing) {
        if (missing <= 0) {
            return;
        }

        List<ProvidedKeyTemplate> generatedKeys = generateKeysForType(now, forType, missing);
        keys.add(generatedKeys);
        log.info("Generated keys: {}", nameAndType(generatedKeys));
    }

    private List<ProvidedKeyTemplate> generateKeysForType(Instant now, KeyType forType, int count) {
        return IntStream.range(0, count).boxed()
                .map(it -> generator.generateValidKey(now, forType))
                .collect(Collectors.toList());
    }

    private KeyStore readOrCreateKeystoreIfMissing() {
        KeyStore ks = persistence.read();
        if (null == ks) {
            ks = juggler.toKeystore().generate(KeySet.builder().build());
        }

        return ks;
    }

    private AliasWithMeta<KeyState> toStatus(KeyEntry key, KeyStatus status) {
        AliasWithMeta<KeyState> current = key.aliasWithMeta(KeyState.class);
        current.toBuilder().metadata(current.getMetadata().toBuilder().status(status).build());
        return current;
    }

    private List<String> nameAndType(ResultCollection<KeyEntry> collection) {
        return collection.stream()
                .map(it -> it.getMeta(KeyState.class).getType().name() + ":" + it.getAlias())
                .collect(Collectors.toList());
    }

    private List<String> nameAndType(List<ProvidedKeyTemplate> collection) {
        return collection.stream()
                .map(it -> ((KeyState) it.getMetadata()).getType().name() + ":" + it.generateName())
                .collect(Collectors.toList());
    }
}
