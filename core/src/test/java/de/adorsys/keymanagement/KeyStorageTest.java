package de.adorsys.keymanagement;

import de.adorsys.keymanagement.core.KeyGenerator;
import de.adorsys.keymanagement.core.KeySet;
import de.adorsys.keymanagement.core.KeySetTemplate;
import de.adorsys.keymanagement.core.collection.keystore.view.AliasView;
import de.adorsys.keymanagement.core.collection.keystore.view.KeyView;
import de.adorsys.keymanagement.core.impl.EncryptingKeyGeneratorImpl;
import de.adorsys.keymanagement.core.impl.KeyStoreOperImpl;
import de.adorsys.keymanagement.core.impl.SecretKeyGeneratorImpl;
import de.adorsys.keymanagement.core.impl.SigningKeyGeneratorImpl;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
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
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static de.adorsys.keymanagement.core.collection.keystore.KeyAlias.A_ID;
import static de.adorsys.keymanagement.core.collection.keystore.QueryableKey.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class KeyStorageTest {

    @Test
    @SneakyThrows
    void basicKeystoreTest() {
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

        val store = new KeyStoreOperImpl(password).generate(keySet);
        val keyView = new KeyView(store, new KeyStoreOperImpl(password), password.get());

        assertThat(keyView.retrieve(equal(IS_SECRET, true)).toCollection()).hasSize(2);
        assertThat(keyView.retrieve(equal(IS_TRUST_CERT, true))).hasSize(0);
        assertThat(keyView.retrieve(equal(IS_PRIVATE, true))).hasSize(12);
        assertThat(keyView.retrieve(equal(HAS_VALID_CERTS, true))).hasSize(12);
        assertThat(keyView.retrieve(equal(HAS_VALID_CERTS, true)).toCollection().pickNrandom(2)).hasSize(2);
        assertThat(keyView.retrieve(has(CERT))).hasSize(12);
        assertThat(keyView.privateKeys()).hasSize(12);
        assertThat(keyView.privateKeys().pickNrandom(2)).hasSize(2);
        // FIXME - this key is of signing type - we need to allow to differentiate between signing and encryption keys
        assertThat(keyView.privateKeys().hasAlias(it -> it.startsWith("Z"))).hasSize(1);
        assertThat(keyView.publicKeys()).hasSize(12);
        assertThat(keyView.secretKeys()).hasSize(2);
        assertThat(keyView.trustedCerts()).hasSize(0);
        assertThat(keyView.retrieve("SELECT * FROM keys WHERE getAlias LIKE 'Z%'")).hasSize(3);
        assertThat(keyView.retrieve("SELECT * FROM keys WHERE getKey IS NOT NULL")).hasSize(14);
        assertThat(keyView.retrieve("SELECT * FROM keys WHERE getKey IS NOT NULL")).hasSize(14);
    }

    @Test
    @SneakyThrows
    void updateKeystoreThroughAliasTest() {
        Security.addProvider(new BouncyCastleProvider());

        Supplier<char[]> password = "Password"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .generatedSecretKeys(Secret.with().prefix("SSSS").build().repeat(10))
                .generatedEncryptionKeys(Encrypting.with().prefix("EEEE").build().repeat(10))
                .build();
        KeySet keySet = new KeyGenerator(
                new EncryptingKeyGeneratorImpl(),
                new SecretKeyGeneratorImpl(),
                new SigningKeyGeneratorImpl()
        ).generate(template);

        val store = new KeyStoreOperImpl(password).generate(keySet);
        val keyView = new AliasView(store, new KeyStoreOperImpl(password));

        assertThat(countKeysByPrefixInKeystore(store, "EEEE")).isEqualTo(10);

        val privateKeys = keyView.retrieve(startsWith(A_ID, "EEEE")).toCollection();
        keyView.update(
                privateKeys,
                Collections.emptyList()
        );

        assertThat(countKeysByPrefixInKeystore(store, "EEEE")).isZero();

        keyView.update(
                Collections.emptyList(),
                Collections.singleton(Provided.with().alias("QQQQ").key(stubSecretKey()).build())
        );

        assertThat(countKeysByPrefixInKeystore(store, "QQQQ")).isEqualTo(1);
    }

    @Test
    @SneakyThrows
    void updateKeystoreTest() {
        Security.addProvider(new BouncyCastleProvider());

        Supplier<char[]> password = "Password"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .generatedSecretKeys(Secret.with().prefix("SSSS").build().repeat(10))
                .generatedEncryptionKeys(Encrypting.with().prefix("EEEE").build().repeat(10))
                .build();
        KeySet keySet = new KeyGenerator(
                new EncryptingKeyGeneratorImpl(),
                new SecretKeyGeneratorImpl(),
                new SigningKeyGeneratorImpl()
        ).generate(template);

        val store = new KeyStoreOperImpl(password).generate(keySet);
        val keyView = new KeyView(store, new KeyStoreOperImpl(password), password.get());

        assertThat(keyView.privateKeys()).hasSize(10);
        assertThat(keyView.secretKeys()).hasSize(10);

        val privateKeys = keyView.privateKeys().asQueryable();
        keyView.update(
                privateKeys,
                Collections.emptyList()
        );

        assertThat(keyView.privateKeys()).hasSize(0);
        assertThat(countKeysByPrefixInKeystore(store, "EEEE")).isZero();

        keyView.update(
                Collections.emptyList(),
                privateKeys.stream().map(
                        it -> ProvidedKeyEntry.with()
                                .prefix("NNNN")
                                .entry(it.getKey())
                                .password(password).build()
                ).collect(Collectors.toList())
        );

        assertThat(keyView.privateKeys()).hasSize(10);
        assertThat(countKeysByPrefixInKeystore(store, "NNNN")).isEqualTo(10);
    }

    @SneakyThrows
    private int countKeysByPrefixInKeystore(KeyStore keyStore, String prefix) {
        Enumeration<String> aliases = keyStore.aliases();
        int result = 0;
        while (aliases.hasMoreElements()) {
            result += aliases.nextElement().startsWith(prefix) ? 1 : 0;
        }

        return result;
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}