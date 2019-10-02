package de.adorsys.keymanagement.generator;

import de.adorsys.keymanagement.KeyStorageTemplate;
import de.adorsys.keymanagement.generator.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.generator.deprecated.types.keystore.SecretKeyEntry;
import de.adorsys.keymanagement.generator.generator.*;
import de.adorsys.keymanagement.generator.types.KeyPairEntry;
import de.adorsys.keymanagement.template.generated.Encrypting;
import de.adorsys.keymanagement.template.generated.Secret;
import de.adorsys.keymanagement.template.generated.Signing;
import de.adorsys.keymanagement.template.provided.Provided;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bouncycastle.cert.X509CertificateHolder;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeyStorageGenerator {

    private static final String PASSWORD = "password";

    @SneakyThrows
    public KeyStore generate(KeyStorageTemplate template) {
        KeyStore ks = KeyStore.getInstance("UBER");
        ks.load(null);

        template.providedKeys().forEach(it -> addProvidedEntry(ks, it));
        template.generatedSecretKeys().forEach(it -> addGeneratedSecret(ks, it));
        template.generatedEncryptionKeys().forEach(it -> addGeneratedEncrypting(ks, it));
        template.generatedSigningKeys().forEach(it -> addGeneratedSigning(ks, it));
        return ks;
    }

    @SneakyThrows
    private void addProvidedEntry(KeyStore ks, @NonNull Provided template) {
        ks.setEntry(
                keyName(template.getAlias(), template.getPrefix()),
                new KeyStore.SecretKeyEntry((SecretKey) template.getKey()), // FIXME - generify
                new KeyStore.PasswordProtection(PASSWORD.toCharArray()));
    }

    @SneakyThrows
    private void addGeneratedSecret(KeyStore ks, Secret template) {
        addToKeyStore(ks, generateSecret(template));
    }

    @SneakyThrows
    private void addGeneratedEncrypting(KeyStore ks, Encrypting template) {
        addToKeyStore(ks, generateEncrypting(template));
    }

    @SneakyThrows
    private void addGeneratedSigning(KeyStore ks, Signing template) {
        addToKeyStore(ks, generateSigning(template));
    }

    private SecretKeyData generateSecret(Secret secret) {
        return new SecretKeyGeneratorImpl(secret.getAlgo(), secret.getSize())
                .generate(
                        keyName(secret.getAlias(), secret.getPrefix()),
                        new ReadKeyPassword("read-pass")
                );
    }

    @SneakyThrows
    private KeyPairData generateEncrypting(Encrypting encrypting) {
        return new KeyPairGeneratorImpl(encrypting.getAlgo(), encrypting.getSize(), encrypting.getSigAlgo(), "PAIR")
                .generateEncryptionKey(encrypting.getAlias(), new ReadKeyPassword("read-pass"));
    }

    @SneakyThrows
    private KeyPairData generateSigning(Signing signing) {
        return new KeyPairGeneratorImpl(signing.getAlgo(), signing.getSize(), signing.getSigAlgo(), "PAIR")
                .generateSignatureKey(signing.getAlias(), new ReadKeyPassword("read-pass"));
    }

    @SneakyThrows
    private static void addToKeyStore(final KeyStore ks, KeyPairEntry keyPairHolder) {
        List<Certificate> chainList = new ArrayList<>();
        X509CertificateHolder subjectCert = keyPairHolder.getKeyPair().getSubjectCert();
        chainList.add(V3CertificateUtils.getX509JavaCertificate(subjectCert));
        Certificate[] chain = chainList.toArray(new Certificate[0]);
        ks.setKeyEntry(keyPairHolder.getAlias(), keyPairHolder.getKeyPair().getKeyPair().getPrivate(),
                keyPairHolder.getReadKeyPassword().getValue().toCharArray(), chain);
    }

    @SneakyThrows
    private static void addToKeyStore(KeyStore ks, SecretKeyEntry secretKeyData) {
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKeyData.getSecretKey());
        KeyStore.ProtectionParameter protParam = getPasswordProtectionParameter(secretKeyData.getReadKeyPassword());
        ks.setEntry(secretKeyData.getAlias(), entry, protParam);
    }

    private static KeyStore.ProtectionParameter getPasswordProtectionParameter(ReadKeyPassword readKeyPassword) {
        return new KeyStore.PasswordProtection(readKeyPassword.getValue().toCharArray());
    }

    private String keyName(String alias, String prefix) {
        return null != alias ? alias : generateName(prefix);
    }

    private String generateName(String prefix) {
        return null != prefix ? prefix + UUID.randomUUID().toString() : UUID.randomUUID().toString();
    }
}
