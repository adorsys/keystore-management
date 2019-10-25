package de.adorsys.keymanagement.examples;

import de.adorsys.keymanagement.api.types.template.generated.Pbe;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.juggler.services.DaggerJuggler;
import de.adorsys.keymanagement.juggler.services.Juggler;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.interfaces.PBEKey;
import java.security.Key;
import java.security.Security;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class GeneratePbeKeyTest {

    @Test
    void newPbe() {
        Security.addProvider(new BouncyCastleProvider());
        // BEGIN_SNIPPET:Store your own char array securely in KeyStore
        // Obtain Juggler service instance:
        Juggler juggler = DaggerJuggler.builder().build();
        // Generate PBE (password-based encryption) key:
        Key key = juggler.generateKeys().secret(
                Pbe.with()
                        .alias("AES-KEY") // with alias `AES-KEY` if we will save it to keystore from KeySet
                        .data("MY SECRET DATA".toCharArray()) // This data will be encrypted inside KeyStore when stored
                        .build()
        ).getKey();

        assertThat(key.getAlgorithm()).isEqualTo("PBEWithSHA256And256BitAES-CBC-BC");
        assertThat(((PBEKey) key).getPassword()).isEqualTo("MY SECRET DATA".toCharArray());
        // END_SNIPPET
    }
}
