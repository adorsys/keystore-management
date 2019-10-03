package de.adorsys.keymanagement.generator;

import de.adorsys.keymanagement.KeyStorageTemplate;
import de.adorsys.keymanagement.generator.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.generator.deprecated.types.keystore.SecretKeyEntry;
import de.adorsys.keymanagement.generator.generator.*;
import de.adorsys.keymanagement.generator.types.KeyPairEntry;
import de.adorsys.keymanagement.template.NamedWithPassword;
import de.adorsys.keymanagement.template.generated.Encrypting;
import de.adorsys.keymanagement.template.generated.Secret;
import de.adorsys.keymanagement.template.generated.Signing;
import de.adorsys.keymanagement.template.provided.KeyEntry;
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

    @SneakyThrows
    public KeyStore generate(KeyStorageTemplate template) {
        KeyStore ks = KeyStore.getInstance("UBER");
        ks.load(null);

        // FIXME - key ids uniqueness validation
        template.providedKeys().forEach(it -> addProvidedKey(ks, it, extractPassword(it, template)));
        template.providedKeyEntries().forEach(it -> addProvidedEntry(ks, it, extractPassword(it, template)));
        template.generatedSecretKeys().forEach(it -> addGeneratedSecret(ks, it, extractPassword(it, template)));
        template.generatedEncryptionKeys().forEach(it -> addGeneratedEncrypting(ks, it, extractPassword(it, template)));
        template.generatedSigningKeys().forEach(it -> addGeneratedSigning(ks, it, extractPassword(it, template)));
        return ks;
    }

    @SneakyThrows
    private void addProvidedKey(KeyStore ks, @NonNull Provided template, char[] password) {
        ks.setEntry(
                keyName(template.getAlias(), template.getPrefix()),
                new KeyStore.SecretKeyEntry((SecretKey) template.getKey()), // FIXME - generify
                new KeyStore.PasswordProtection(password)
        );
    }

    @SneakyThrows
    private void addProvidedEntry(KeyStore ks, @NonNull KeyEntry template, char[] password) {
        ks.setEntry(
                keyName(template.getAlias(), template.getPrefix()),
                template.getEntry(),
                new KeyStore.PasswordProtection(password));
    }

    @SneakyThrows
    private void addGeneratedSecret(KeyStore ks, Secret template, char[] password) {
        addToKeyStore(ks, generateSecret(template, password), password);
    }

    @SneakyThrows
    private void addGeneratedEncrypting(KeyStore ks, Encrypting template, char[] password) {
        addToKeyStore(ks, generateEncrypting(template, password), password);
    }

    @SneakyThrows
    private void addGeneratedSigning(KeyStore ks, Signing template, char[] password) {
        addToKeyStore(ks, generateSigning(template, password), password);
    }

    private SecretKeyData generateSecret(Secret secret, char[] password) {
        return new SecretKeyGeneratorImpl(secret.getAlgo(), secret.getSize())
                .generate(
                        keyName(secret.getAlias(), secret.getPrefix()),
                        new ReadKeyPassword(password)
                );
    }

    @SneakyThrows
    private KeyPairData generateEncrypting(Encrypting encrypting, char[] password) {
        return new KeyPairGeneratorImpl(encrypting.getAlgo(), encrypting.getSize(), encrypting.getSigAlgo(), "PAIR")
                .generateEncryptionKey(encrypting.getAlias(), new ReadKeyPassword(password));
    }

    @SneakyThrows
    private KeyPairData generateSigning(Signing signing, char[] password) {
        return new KeyPairGeneratorImpl(signing.getAlgo(), signing.getSize(), signing.getSigAlgo(), "PAIR")
                .generateSignatureKey(signing.getAlias(), new ReadKeyPassword(password));
    }

    @SneakyThrows
    private static void addToKeyStore(final KeyStore ks, KeyPairEntry keyPairHolder, char[] password) {
        List<Certificate> chainList = new ArrayList<>();
        X509CertificateHolder subjectCert = keyPairHolder.getKeyPair().getSubjectCert();
        chainList.add(V3CertificateUtils.getX509JavaCertificate(subjectCert));
        Certificate[] chain = chainList.toArray(new Certificate[0]);
        ks.setKeyEntry(keyPairHolder.getAlias(), keyPairHolder.getKeyPair().getKeyPair().getPrivate(),
                password, chain);
    }

    @SneakyThrows
    private static void addToKeyStore(KeyStore ks, SecretKeyEntry secretKeyData, char[] password) {
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKeyData.getSecretKey());
        KeyStore.ProtectionParameter protParam = getPasswordProtectionParameter(new ReadKeyPassword(password));
        ks.setEntry(secretKeyData.getAlias(), entry, protParam);
    }

    private static KeyStore.ProtectionParameter getPasswordProtectionParameter(ReadKeyPassword readKeyPassword) {
        return new KeyStore.PasswordProtection(readKeyPassword.getValue());
    }

    private String keyName(String alias, String prefix) {
        return null != alias ? alias : generateName(prefix);
    }

    private String generateName(String prefix) {
        return null != prefix ? prefix + UUID.randomUUID().toString() : UUID.randomUUID().toString();
    }

    private char[] extractPassword(NamedWithPassword source, KeyStorageTemplate template) {
        // FIXME Null check
        return (null != source.getPassword() ? source.getPassword() : template.keyPassword()).get();
    }
}
