package de.adorsys.keymanagement.examples;

import com.googlecode.cqengine.attribute.support.SimpleFunction;
import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.api.keystore.KeyStoreView;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.view.AliasView;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.core.metadata.WithPersister;
import de.adorsys.keymanagement.juggler.services.BCJuggler;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.time.Instant;
import java.util.function.Supplier;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static de.adorsys.keymanagement.core.view.AliasViewImpl.META;
import static org.assertj.core.api.Assertions.assertThat;

class PersistMetadataToKeyStoreTest {

    @Test
    void newKeystore() {
        // BEGIN_SNIPPET:Save metadata to keystore
        // Obtain Juggler service
        BCJuggler juggler = DaggerBCJuggler.builder()
                .metadataPersister(new WithPersister()) // enable metadata persistence
                .metadataConfig(
                        MetadataPersistenceConfig.builder()
                                .metadataClass(KeyExpirationMetadata.class) // define metadata class
                                .build()
                )
                .build();

        // Key set template that is going to be saved into KeyStore
        KeySetTemplate template = KeySetTemplate.builder()
                // One private key that can be used for encryption:
                .generatedEncryptionKey(
                        Encrypting.with()
                                .alias("ENC-KEY-1") // key with alias `ENC-KEY-1` in KeyStore
                                .metadata(new KeyExpirationMetadata(Instant.now())) // Associated metadata with this key, pretend it is `expired` key
                                .build()
                )
                .build();

        // Generate key set:
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        // Key protection password:
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        // Generate new KeyStore, it will have metadata in it
        KeyStore ks = juggler.toKeystore().generate(keySet, password);
        // Open KeyStore view to query it:
        KeyStoreView source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        // Open alias view to query key alias by metadata
        AliasView<Query<KeyAlias>> view = source.aliases();
        // Assert that key has been expired
        SimpleFunction<KeyAlias, Instant> function = key -> ((KeyExpirationMetadata) key.getMeta()).getExpiresAfter();
        assertThat(
                view.retrieve(
                        and(
                                has(META), // Key has metadata
                                lessThan(
                                        attribute(KeyAlias.class, Instant.class, "instant", function), // Key expiration date
                                        Instant.now() // current date, so that if expiresAfter < now() key is expired
                                )
                        )
                ).toCollection()
        ).hasSize(1);
        // END_SNIPPET
    }

    @Getter
    @RequiredArgsConstructor
    private static class KeyExpirationMetadata implements KeyMetadata {

        private final Instant expiresAfter;
    }
}
