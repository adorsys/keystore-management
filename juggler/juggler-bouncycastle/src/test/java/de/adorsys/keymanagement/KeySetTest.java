package de.adorsys.keymanagement;

import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.api.keystore.KeyStoreView;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.core.metadata.WithPersister;
import de.adorsys.keymanagement.core.source.DefaultKeyStoreView;
import de.adorsys.keymanagement.juggler.services.BCJuggler;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.KeyStore;
import java.time.Instant;
import java.util.Collections;
import java.util.function.Supplier;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.core.view.AliasViewImpl.A_ID;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class KeySetTest {

    @Test
    @SneakyThrows
    void basicKeySetTest() {
        BCJuggler juggler = DaggerBCJuggler.builder().build();

        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(ProvidedKey.with().prefix("ZZZ").key(stubSecretKey()).build())
                .generatedSecretKey(Secret.with().prefix("ZZZ").build())
                .generatedSigningKey(Signing.with().algo("DSA").alias("ZZZ").build())
                .generatedEncryptionKey(Encrypting.with().alias("TTT").build())
                .generatedEncryptionKeys(Encrypting.with().prefix("TTT").build().repeat(10))
                .build();

        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        KeyStore store = juggler.toKeystore().generate(keySet, password);
        KeyStoreView source = juggler.readKeys().fromKeyStore(store, id -> password.get());
        EntryView<Object> entryView = source.entries();

        assertThat(entryView.all()).hasSize(14);
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias LIKE 'Z%'").toCollection()).hasSize(3);
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias LIKE 'TTT%'").toCollection()).hasSize(11);
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE is_secret = true").toCollection()).hasSize(2);

        entryView.add(ProvidedKey.with().password(password).alias("MMM").key(stubSecretKey()).build());

        assertThat(entryView.retrieve("SELECT * FROM keys WHERE is_secret = true").toCollection()).hasSize(3);
        assertThat(source.aliases().all()).hasSize(15);

        assertThat(entryView.privateKeys()).hasSize(12);
        assertThat(entryView.secretKeys()).hasSize(3);
        assertThat(entryView.trustedCerts()).hasSize(0);
    }

    @Test
    @SneakyThrows
    void cloningTest() {
        BCJuggler juggler = DaggerBCJuggler.builder().build();

        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(ProvidedKey.with().prefix("ZZZ").key(stubSecretKey()).build())
                .generatedSecretKey(Secret.with().prefix("ZZZ").build())
                .generatedSigningKey(Signing.with().algo("DSA").alias("ZZZ").build())
                .generatedEncryptionKey(Encrypting.with().alias("TTT").build())
                .generatedEncryptionKeys(Encrypting.with().prefix("TTT").build().repeat(10))
                .build();

        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val store = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(store, id -> password.get());
        val cloned = juggler.toKeystore().generate(source.copyToKeySet(id -> password.get()));

        assertThat(juggler.readKeys().fromKeyStore(cloned, id -> password.get()).entries().all()).hasSize(14);
        assertThat(juggler.readKeys().fromKeyStore(cloned, id -> password.get()).aliases().all()).hasSize(14);
    }

    @Test
    @SneakyThrows
    void metadataTest() {
        BCJuggler juggler = DaggerBCJuggler.builder()
                .metadataPersister(new WithPersister())
                .metadataConfig(
                        MetadataPersistenceConfig.builder()
                                .metadataClass(Metadata.class)
                                .build()
                )
                .build();

        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(ProvidedKey.with()
                        .prefix("ZZZ").key(stubSecretKey()).metadata(new Metadata(Instant.now()))
                        .build()
                ).generatedEncryptionKey(
                        Encrypting.with().alias("TTT").metadata(new Metadata(Instant.now())).build()
                )
                .build();
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        val view = source.aliases();
        assertThat(
                ((Metadata)
                        view.uniqueResult(equal(A_ID, "TTT")).getMeta()
                ).getExpiresAfter()
        ).isBefore(Instant.now());
    }

    @Test
    @SneakyThrows
    void metadataEntriesTest() {
        BCJuggler juggler = DaggerBCJuggler.builder()
                .metadataPersister(new WithPersister())
                .metadataConfig(
                        MetadataPersistenceConfig.builder()
                                .metadataClass(Metadata.class)
                                .build()
                )
                .build();

        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .generatedEncryptionKey(
                        Encrypting.with().alias("TTT").metadata(new Metadata(Instant.now())).build()
                )
                .build();

        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        val view = source.entries();

        assertThat(
                ((Metadata)
                        view.uniqueResult(equal(A_ID, "TTT")).getMeta()
                ).getExpiresAfter()
        ).isBefore(Instant.now());
    }

    @Test
    @SneakyThrows
    void metadataUpdateTest() {
        BCJuggler juggler = DaggerBCJuggler.builder()
                .metadataPersister(new WithPersister())
                .metadataConfig(
                        MetadataPersistenceConfig.builder()
                                .metadataClass(Metadata.class)
                                .build()
                )
                .build();

        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .generatedEncryptionKey(
                        Encrypting.with().alias("TTT").metadata(new Metadata(Instant.now())).build()
                )
                .build();

        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        val view = source.entries();

        val oldMetadata = view.uniqueResult(equal(A_ID, "TTT")).aliasWithMeta(Metadata.class);
        val newMetadata = oldMetadata.toBuilder().metadata(new Metadata(Instant.MIN)).build();
        view.update(Collections.singleton(newMetadata));

        assertThat(view.uniqueResult(equal(A_ID, "TTT")).getMeta(Metadata.class).getExpiresAfter())
                .isEqualTo(Instant.MIN);

        // validate that it was persisted to KeyStore
        val aliasView = source.aliases();
        assertThat(aliasView.uniqueResult(equal(A_ID, "TTT")).getMeta(Metadata.class).getExpiresAfter())
                .isEqualTo(Instant.MIN);
    }

    @Getter
    @RequiredArgsConstructor
    private static class Metadata implements KeyMetadata {

        private final Instant expiresAfter;
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}