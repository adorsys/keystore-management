package de.adorsys.keymanagement;

import com.googlecode.cqengine.query.QueryFactory;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.core.metadata.WithPersister;
import de.adorsys.keymanagement.juggler.services.DaggerJuggler;
import de.adorsys.keymanagement.juggler.services.Juggler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.Security;
import java.time.Instant;
import java.util.function.Supplier;

import static de.adorsys.keymanagement.core.view.AliasViewImpl.A_ID;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class KeySetTest {

    @Test
    @SneakyThrows
    void basicKeySetTest() {
        Security.addProvider(new BouncyCastleProvider());
        Juggler juggler = DaggerJuggler.builder().build();

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
        val entryView = source.entries();

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
        Security.addProvider(new BouncyCastleProvider());

        Juggler juggler = DaggerJuggler.builder().build();

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
        Security.addProvider(new BouncyCastleProvider());

        Juggler juggler = DaggerJuggler.builder()
                .metadataPersister(new WithPersister())
                .metadataConfig(
                        MetadataPersistenceConfig.builder()
                                .metadataClass(KeyExpirationMetadata.class)
                                .build()
                )
                .build();

        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(ProvidedKey.with()
                        .prefix("ZZZ").key(stubSecretKey()).metadata(new KeyExpirationMetadata(Instant.now()))
                        .build()
                ).generatedEncryptionKey(
                        Encrypting.with().alias("TTT").metadata(new KeyExpirationMetadata(Instant.now())).build()
                )
                .build();
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        val ks = juggler.toKeystore().generate(keySet, password);
        val source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
       val view = source.aliases();
        view.retrieve(QueryFactory.equal(A_ID, "TTT")).toCollection().first().getAlias();
        assertThat(
                ((KeyExpirationMetadata)
                        view.retrieve(QueryFactory.equal(A_ID, "TTT")).toCollection().first().getMeta()
                ).getExpiresAfter()
        ).isBefore(Instant.now());
    }

    @Getter
    @RequiredArgsConstructor
    private static class KeyExpirationMetadata implements KeyMetadata {

        private final Instant expiresAfter;
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}