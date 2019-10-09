package de.adorsys.keymanagement;

import de.adorsys.keymanagement.core.KeyGenerator;
import de.adorsys.keymanagement.core.KeySet;
import de.adorsys.keymanagement.core.KeySetTemplate;
import de.adorsys.keymanagement.core.collection.KeySetView;
import de.adorsys.keymanagement.core.impl.EncryptingKeyGeneratorImpl;
import de.adorsys.keymanagement.core.impl.SecretKeyGeneratorImpl;
import de.adorsys.keymanagement.core.impl.SigningKeyGeneratorImpl;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.Provided;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.Security;
import java.util.function.Supplier;

import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.core.collection.QueryableProvided.IS_PRIVATE;
import static de.adorsys.keymanagement.core.collection.QueryableProvided.IS_SECRET;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class KeySetTest {

    @Test
    @SneakyThrows
    void basicKeySetTest() {
        Security.addProvider(new BouncyCastleProvider());

        Supplier<char[]> password = "Password"::toCharArray;
        Supplier<char[]> password2 = "Password Other"::toCharArray;

        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(Provided.with().prefix("ZZZ").key(stubSecretKey()).build())
                .generatedSecretKey(Secret.with().prefix("ZZZ").build())
                .generatedSigningKey(Signing.with().algo("DSA").alias("ZZZ").build())
                .generatedEncryptionKey(Encrypting.with().password(password).alias("TTT").build())
                .generatedEncryptionKeys(Encrypting.with().password(password).prefix("TTT").build().repeat(10))
                .build();

        KeySet keySet = new KeyGenerator(
                new EncryptingKeyGeneratorImpl(),
                new SecretKeyGeneratorImpl(),
                new SigningKeyGeneratorImpl()
        ).generate(template);

        val keyView = new KeySetView(keySet);

        assertThat(keyView.retrieve(equal(IS_SECRET, true)).toCollection()).hasSize(2);
        assertThat(keyView.retrieve(equal(IS_PRIVATE, true))).hasSize(12);
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}