package de.adorsys.keymanagement;

import de.adorsys.keymanagement.generator.KeyStorageGenerator;
import de.adorsys.keymanagement.template.generated.Encrypting;
import de.adorsys.keymanagement.template.generated.Signing;
import de.adorsys.keymanagement.template.provided.Provided;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.KeyStore;
import java.security.Security;

@Slf4j
class KeyStorageTest {

    @Test
    @SneakyThrows
    void basicTest() {
        Security.addProvider(new BouncyCastleProvider());

        KeyStorageTemplate template = KeyStorageTemplate.builder()
                .providedKey(Provided.with().prefix("ZZZ").key(stubSecretKey()).build())
                .generatedSigningKey(Signing.with().algo("DSA").alias("ZZZ").build())
                .generatedEncryptionKey(Encrypting.with().alias("TTT").build())
                .build();

        KeyStore store = new KeyStorageGenerator().generate(template);

        log.info("Arrr! {}", store.aliases());
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}