package de.adorsys.keymanagement;

import de.adorsys.keymanagement.collection.KeyView;
import de.adorsys.keymanagement.generator.KeyStorageGenerator;
import de.adorsys.keymanagement.template.generated.Encrypting;
import de.adorsys.keymanagement.template.generated.Secret;
import de.adorsys.keymanagement.template.generated.Signing;
import de.adorsys.keymanagement.template.provided.Provided;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.KeyStore;
import java.security.Security;
import java.util.function.Supplier;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.collection.QueryableKey.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class KeyStorageTest {

    @Test
    @SneakyThrows
    void basicTest() {
        Security.addProvider(new BouncyCastleProvider());

        Supplier<char[]> password = "Password"::toCharArray;
        Supplier<char[]> password2 = "Password Other"::toCharArray;

        KeyStorageTemplate template = KeyStorageTemplate.builder()
                .keyPassword(password)
                .providedKey(Provided.with().prefix("ZZZ").key(stubSecretKey()).build())
                .generatedSecretKey(Secret.with().prefix("ZZZ").build())
                .generatedSigningKey(Signing.with().algo("DSA").alias("ZZZ").build())
                .generatedEncryptionKey(Encrypting.with().password(password).alias("TTT").build())
                .generatedEncryptionKeys(Encrypting.with().password(password).prefix("TTT").build().repeat(10))
                .build();

        KeyStore store = new KeyStorageGenerator().generate(template);
        val keyView = new KeyView(store, password.get());

        assertThat(keyView.retrieve(equal(IS_SECRET, true))).hasSize(2);
        assertThat(keyView.retrieve(equal(IS_TRUST_CERT, true))).hasSize(0);
        assertThat(keyView.retrieve(equal(IS_PRIVATE, true))).hasSize(12);
        log.info("Arrr! {}", store.aliases());
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}