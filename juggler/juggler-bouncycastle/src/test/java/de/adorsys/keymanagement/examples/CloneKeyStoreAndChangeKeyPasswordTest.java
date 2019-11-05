package de.adorsys.keymanagement.examples;

import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.juggler.services.BCJuggler;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class CloneKeyStoreAndChangeKeyPasswordTest {

    @Test
    @SneakyThrows
    void cloneKeystoreAndChangeKeyPassword() {
        // BEGIN_SNIPPET:Clone keystore and change key password
        // Obtain Juggler service instance:
        BCJuggler juggler = DaggerBCJuggler.builder().build();

        // We want our keystore to have:
        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(ProvidedKey.with().alias("MY-KEY").key(stubSecretKey()).build()) // One provided key (i.e. existing) that has alias `MY-KEY`
                .generatedEncryptionKeys(Encrypting.with().prefix("ENC").build().repeat(10)) // Ten generated private keys (with certificates) that have alias `ENC` + random UUID
                .build();

        // Provide key protection password:
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        // Generate key set
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        // Generate KeyStore with each key protected with `PASSWORD!` password
        KeyStore store = juggler.toKeystore().generate(keySet, password);
        // Clone generated KeyStore:
        Supplier<char[]> newPassword = "NEW_PASSWORD!"::toCharArray;
        // Create key set from old keystore that has new password `NEW_PASSWORD!`:
        KeySet clonedSet = juggler.readKeys()
                .fromKeyStore(store, id -> password.get())
                .copyToKeySet(id -> newPassword.get());
        // Generate cloned KeyStore with each key protected with `NEW_PASSWORD!` password (provided on key set)
        KeyStore newKeystore = juggler.toKeystore().generate(clonedSet, () -> null);

        // Validate old keystore has same key count as new keystore:
        assertThat(countKeys(store)).isEqualTo(countKeys(newKeystore));
        // Validate old keystore has key password `PASSWORD!`
        assertThat(store.getKey("MY-KEY", "PASSWORD!".toCharArray())).isNotNull();
        // Validate new keystore has key password `NEW_PASSWORD!`
        assertThat(newKeystore.getKey("MY-KEY", "NEW_PASSWORD!".toCharArray())).isNotNull();
        // END_SNIPPET
    }

    @SneakyThrows
    private int countKeys(KeyStore keyStore) {
        Enumeration<String> aliases = keyStore.aliases();
        int count = 0;
        while (aliases.hasMoreElements()) {
            aliases.nextElement();
            count++;
        }

        return count;
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}
