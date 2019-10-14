package de.adorsys.keymanagement.core.generator;

import de.adorsys.keymanagement.api.SigningKeyGenerator;
import de.adorsys.keymanagement.core.deprecated.generator.KeyPairData;
import de.adorsys.keymanagement.core.deprecated.generator.KeyPairGeneratorImpl;
import de.adorsys.keymanagement.core.deprecated.generator.V3CertificateUtils;
import de.adorsys.keymanagement.core.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import lombok.SneakyThrows;
import org.bouncycastle.cert.X509CertificateHolder;

import java.security.KeyPair;

public class SigningKeyGeneratorImpl implements SigningKeyGenerator {

    @Override
    public KeyPair generatePair(Signing fromTemplate) {
        return generateSigning(fromTemplate).getKeyPair().getKeyPair();
    }

    @Override
    public ProvidedKeyPair generate(Signing fromTemplate) {
        KeyPairData data = generateSigning(fromTemplate);
        X509CertificateHolder subjectCert = data.getKeyPair().getSubjectCert();
        return ProvidedKeyPair.builder()
                .keyTemplate(fromTemplate)
                .pair(data.getKeyPair().getKeyPair())
                .certificate(V3CertificateUtils.getX509JavaCertificate(subjectCert))
                .build();
    }

    @SneakyThrows
    private KeyPairData generateSigning(Signing signing) {
        return new KeyPairGeneratorImpl(signing.getAlgo(), signing.getSize(), signing.getSigAlgo(), "PAIR")
                .generateSignatureKey("STUB",
                        new ReadKeyPassword("STUB") // FIXME unneeded
                );
    }
}
