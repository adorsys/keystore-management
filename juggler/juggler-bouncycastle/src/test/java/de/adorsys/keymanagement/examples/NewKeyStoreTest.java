package de.adorsys.keymanagement.examples;

import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
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

class NewKeyStoreTest {

    @Test
    void newKeystore() {
        // BEGIN_SNIPPET:Generate keystore
        // Obtain Juggler service instance:
        BCJuggler juggler = DaggerBCJuggler.builder().build();

        // We want our keystore to have:
        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(ProvidedKey.with().alias("MY-KEY").key(stubSecretKey()).build()) // One provided key (i.e. existing) that has alias `MY-KEY`
                .generatedSecretKey(Secret.with().prefix("SEC").build()) // One generated secret key that has alias `SEC` + random UUID
                .generatedSigningKey(Signing.with().algo("DSA").alias("SIGN").build()) // One generated signing key that has alias `SIGN` + random UUID and uses DSA algorithm
                .generatedEncryptionKeys(Encrypting.with().prefix("ENC").build().repeat(10)) // Ten generated private keys (with certificates) that have alias `ENC` + random UUID
                .build();

        // Provide key protection password:
        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        // Generate key set
        KeySet keySet = juggler.generateKeys().fromTemplate(template);
        // Generate KeyStore
        KeyStore store = juggler.toKeystore().generate(keySet, password);
        // Validate that keystore has 13 keys:
        // One provided, one secret, one signing, ten private
        assertThat(countKeys(store)).isEqualTo(13);
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
