package de.adorsys.keymanagement;

import de.adorsys.keymanagement.core.generator.*;
import de.adorsys.keymanagement.core.persist.KeyStoreOperImpl;
import de.adorsys.keymanagement.core.source.KeySet;
import de.adorsys.keymanagement.core.source.KeyStoreSource;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.view.EntryView;
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

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class KeySetTest {

    @Test
    @SneakyThrows
    void basicKeySetTest() {
        Security.addProvider(new BouncyCastleProvider());

        Supplier<char[]> password = "PASSWORD!"::toCharArray;
        KeySetTemplate template = KeySetTemplate.builder()
                .providedKey(ProvidedKey.with().prefix("ZZZ").key(stubSecretKey()).build())
                .generatedSecretKey(Secret.with().prefix("ZZZ").build())
                .generatedSigningKey(Signing.with().algo("DSA").alias("ZZZ").build())
                .generatedEncryptionKey(Encrypting.with().alias("TTT").build())
                .generatedEncryptionKeys(Encrypting.with().prefix("TTT").build().repeat(10))
                .build();

        KeySet keySet = new KeyGenerator(
                new EncryptingKeyGeneratorImpl(),
                new SecretKeyGeneratorImpl(),
                new SigningKeyGeneratorImpl()
        ).generate(template);
        val oper = new KeyStoreOperImpl(password);
        val store = oper.generate(keySet);
        val source = new KeyStoreSource(oper, store, id -> password.get());
        val entryView = new EntryView(source);

        assertThat(entryView.all()).hasSize(14);
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias LIKE 'Z%'").toCollection()).hasSize(3);
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE alias LIKE 'TTT%'").toCollection()).hasSize(11);
        assertThat(entryView.retrieve("SELECT * FROM keys WHERE is_secret = true").toCollection()).hasSize(2);

        entryView.add(ProvidedKey.with().alias("MMM").key(stubSecretKey()).build());

        assertThat(entryView.retrieve("SELECT * FROM keys WHERE is_secret = true").toCollection()).hasSize(3);
        assertThat(source.aliases()).hasSize(15);
    }

    @SneakyThrows
    private SecretKey stubSecretKey() {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("AAA".toCharArray());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256");
        return keyFac.generateSecret(pbeKeySpec);
    }
}