package de.adorsys.keymanagement.examples;

import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.juggler.services.BCJuggler;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.Security;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateSecretKeyTest {

    @Test
    void newSecretKey() {
        // BEGIN_SNIPPET:Generate secret key
        // Obtain Juggler service instance:
        BCJuggler juggler = DaggerBCJuggler.builder().build();
        // Generate key:
        Key key = juggler.generateKeys().secret(
                Secret.with()
                        .alias("AES-KEY") // with alias `AES-KEY` if we will save it to keystore from KeySet
                        .algo("AES") // for AES encryption
                        .keySize(128) // for AES-128 encryption
                        .build()
        ).getKey();

        assertThat(key.getAlgorithm()).isEqualTo("AES");
        assertThat(key.getEncoded()).hasSize(16); // 16 * 8 (sizeof byte) = 128 bits
        // END_SNIPPET
    }
}
