package de.adorsys.keymanagement.examples;

import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.api.keystore.KeyStoreView;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.juggler.services.BCJuggler;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import org.junit.jupiter.api.Test;

import java.security.KeyStore;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class QueryKeyStoreTest {

    @Test
    void newKeystore() {
        // BEGIN_SNIPPET:Query keystore
        // Obtain Juggler service
        BCJuggler juggler = DaggerBCJuggler.builder().build();


        KeySetTemplate template = KeySetTemplate.builder()
                .generatedSecretKey(Secret.with().prefix("SEC").build()) // Secret key to be generated with name `SEC` + random UUID
                .generatedSigningKey(Signing.with().algo("DSA").alias("SIGN-1").build()) // DSA-based signing key with name `SIGN-1`
                .generatedEncryptionKey(Encrypting.with().alias("ENC-1").build()) // Private key with name `ENC-1`
                .generatedEncryptionKeys(Encrypting.with().prefix("GEN").build().repeat(10)) // Ten private keys with name `GEN` + random UUID
                .build();

        // Generate key set from template:
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        // Key protection password:
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        // Create KeyStore
        KeyStore store = juggler.toKeystore().generate(keySet, password);
        // Open KeyStore-view to query it:
        KeyStoreView source = juggler.readKeys().fromKeyStore(store, id -> password.get());
        // Acquire Key-Entry view, so we can query for KeyEntry entities
        EntryView<Query<KeyEntry>> entryView = source.entries();
        // Query for fact that KeyStore has 13 keys in total:
        assertThat(entryView.all()).hasSize(13);
        // Query for fact that KeyStore has 1 key with name `SEC`
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias = 'ENC-1'").toCollection()).hasSize(1);
        // Query for fact that KeyStore has 10 keys with prefix `GEN`
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias LIKE 'GEN%'").toCollection()).hasSize(10);
        // Query for fact that KeyStore has 1 secret key:
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE is_secret = true").toCollection()).hasSize(1);

        // Query for fact that KeyStore has 1 secret key:
        assertThat(entryView.privateKeys()).hasSize(12);
        // Query for fact that KeyStore has 1 secret key:
        assertThat(entryView.secretKeys()).hasSize(1);
        // Query for fact that KeyStore has 0 trusted certs:
        assertThat(entryView.trustedCerts()).hasSize(0);
        // END_SNIPPET
    }
}
