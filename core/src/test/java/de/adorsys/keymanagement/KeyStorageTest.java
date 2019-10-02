package de.adorsys.keymanagement;

import de.adorsys.keymanagement.collection.AliasCollection;
import de.adorsys.keymanagement.collection.WithAlias;
import de.adorsys.keymanagement.generator.KeyStorageGenerator;
import de.adorsys.keymanagement.template.generated.Encrypting;
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

import static com.googlecode.cqengine.query.QueryFactory.startsWith;
import static org.assertj.core.api.Assertions.assertThat;

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

        AliasCollection aliasCollection = new AliasCollection(store);

        val rsZZ = aliasCollection.retrieve(startsWith(WithAlias.A_ID, "ZZ"));
        val rsT = aliasCollection.retrieve(startsWith(WithAlias.A_ID, "T"));

        assertThat(rsZZ).hasSize(2);
        assertThat(rsT).hasSize(1);

        log.info("Arrr! {}", store.aliases());
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}