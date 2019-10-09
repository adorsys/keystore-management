package de.adorsys.keymanagement.core.impl;

import de.adorsys.keymanagement.api.EncryptingKeyGenerator;
import de.adorsys.keymanagement.core.deprecated.generator.KeyPairData;
import de.adorsys.keymanagement.core.deprecated.generator.KeyPairGeneratorImpl;
import de.adorsys.keymanagement.core.deprecated.generator.V3CertificateUtils;
import de.adorsys.keymanagement.core.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import lombok.SneakyThrows;
import org.bouncycastle.cert.X509CertificateHolder;

import java.security.KeyPair;

public class EncryptingKeyGeneratorImpl implements EncryptingKeyGenerator {

    @Override
    public KeyPair generatePair(Encrypting fromTemplate) {
        return generateEncrypting(fromTemplate).getKeyPair().getKeyPair();
    }

    @Override
    public ProvidedKeyPair generate(Encrypting fromTemplate) {
        KeyPairData data = generateEncrypting(fromTemplate);
        X509CertificateHolder subjectCert = data.getKeyPair().getSubjectCert();
        return ProvidedKeyPair.builder()
                .keyTemplate(fromTemplate)
                .pair(data.getKeyPair().getKeyPair())
                .certificate(V3CertificateUtils.getX509JavaCertificate(subjectCert))
                .build();
    }

    @SneakyThrows
    private KeyPairData generateEncrypting(Encrypting encrypting) {
        return new KeyPairGeneratorImpl(encrypting.getAlgo(), encrypting.getSize(), encrypting.getSigAlgo(), "PAIR")
                .generateEncryptionKey(encrypting.getName(),
                        new ReadKeyPassword("STUB") // FIXME unneded
                );
    }
}
