package de.adorsys.keymanagement.examples;

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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.security.Security;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static de.adorsys.keymanagement.core.view.AliasViewImpl.A_ID;
import static de.adorsys.keymanagement.core.view.AliasViewImpl.META;
import static org.assertj.core.api.Assertions.assertThat;

class RotateKeyBasedOnMetadataTest {

    @Test
    void rotateKeyBasedOnMetadata() {
        // BEGIN_SNIPPET:Rotate expired key in keystore
        // Obtain Juggler service
        BCJuggler juggler = DaggerBCJuggler.builder()
                .metadataPersister(new WithPersister()) // enable metadata persistence
                .metadataConfig(
                        MetadataPersistenceConfig.builder()
                                .metadataClass(KeyValidity.class) // define metadata class
                                .build()
                )
                .build();

        // Key protection password:
        Supplier<char[]> password = "PASSWORD!"::toCharArray;

        // Lazy key template:
        Function<Instant, Encrypting> keyTemplate = expiryDate -> Encrypting.with()
                .alias("ENC-KEY-1") // key with alias `ENC-KEY-1` in KeyStore// Associated metadata with this key, pretend it is `expired` key
                .metadata(new KeyValidity(expiryDate))
                .password(password)
                .build();

        // Key set template that is going to be saved into KeyStore
        KeySetTemplate template = KeySetTemplate.builder()
                // One private key that can be used for encryption:
                .generatedEncryptionKey(
                        keyTemplate.apply(Instant.now().minusSeconds(10)) // Will pretend that key has expired
                )
                .build();

        // Generate key set:
        KeySet keySet = juggler.generateKeys().fromTemplate(template); // Key metadata will indicate that key has expired
        // Generate new KeyStore, it will have metadata in it
        KeyStore ks = juggler.toKeystore().generate(keySet, () -> null);
        // Open KeyStore view to query it:
        KeyStoreView source = juggler.readKeys().fromKeyStore(ks, id -> password.get());
        // Open alias view to query key alias by metadata
        AliasView<Query<KeyAlias>> view = source.aliases();
        // Find expired key:
        KeyAlias expired = view.uniqueResult(KeyValidity.EXPIRED);
        // replace expired key:
        view.update(
                Collections.singleton(expired),
                Collections.singleton(
                        juggler.generateKeys().encrypting(
                                keyTemplate.apply(Instant.now().plus(10, ChronoUnit.HOURS)) // Valid for 10 hours from now
                        )
                )
        );
        // validate there is only one `ENC-KEY-1` key
        assertThat(view.retrieve(equal(A_ID, "ENC-KEY-1")).toCollection()).hasSize(1);
        // and this key is NOT expired
        assertThat(view.retrieve(KeyValidity.EXPIRED).toCollection()).hasSize(0);
        // END_SNIPPET
    }

    @Getter
    @RequiredArgsConstructor
    private static class KeyValidity implements KeyMetadata {

        public static final Query<KeyAlias> EXPIRED = and(
                has(META), // Key has metadata
                lessThan(
                        attribute(key -> ((KeyValidity) key.getMeta()).getExpiresAfter()), // Key expiration date
                        Instant.now() // current date, so that if expiresAfter < now() key is expired
                )
        );

        private final Instant expiresAfter;
    }
}
