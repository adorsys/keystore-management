package de.adorsys.keymanagement.bouncycastle.adapter.generator;

import de.adorsys.keymanagement.api.generator.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyPair;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator.KeyPairData;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator.KeyPairGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator.V3CertificateUtils;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore.ReadKeyPassword;
import lombok.SneakyThrows;
import org.bouncycastle.cert.X509CertificateHolder;

import javax.inject.Inject;
import java.security.KeyPair;

public class DefaultEncryptingKeyGeneratorImpl implements EncryptingKeyGenerator {

    @Inject
    public DefaultEncryptingKeyGeneratorImpl() {
    }

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
                .generateEncryptionKey("STUB",
                        new ReadKeyPassword("STUB") // FIXME unneded
                );
    }
}
