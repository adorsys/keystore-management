package de.adorsys.keymanagement.examples;

import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Pbe;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.juggler.services.BCJuggler;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyStore;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratePbeKeyTest {

    @Test
    @SneakyThrows
    void newPbeRaw() {
        // BEGIN_SNIPPET:Store your own char array securely in KeyStore
        // Obtain Juggler service instance:
        BCJuggler juggler = DaggerBCJuggler.builder().build();
        // Generate PBE (password-based encryption) raw key (only transformed to be stored in keystore,
        // encryption IS PROVIDED by keystore - i.e. BCFKS or UBER keystore provide it):
        Supplier<char[]> keyPassword =  "WOW"::toCharArray;
        ProvidedKey key = juggler.generateKeys().secretRaw(
                Pbe.with()
                        .alias("AES-KEY") // with alias `AES-KEY` if we will save it to keystore from KeySet
                        .data("MY SECRET DATA Тест!".toCharArray()) // This data will be encrypted inside KeyStore when stored
                        .password(keyPassword) // Password that will be used to protect key in KeyStore
                        .build()
        );

        // Send key to keystore
        KeyStore ks = juggler.toKeystore().generate(KeySet.builder().key(key).build());

        // Read key back
        SecretKeySpec keyFromKeyStore = (SecretKeySpec) ks.getKey("AES-KEY", keyPassword.get());
        // Note that BouncyCastle keys are encoded in PKCS12 byte format - UTF-16 big endian + 2 0's padding
        assertThat(juggler.decode().decodeAsString(keyFromKeyStore.getEncoded())).isEqualTo("MY SECRET DATA Тест!");
        // END_SNIPPET
    }
}
