package de.adorsys.keymanagement.bouncycastle.adapter.services.generator;

import de.adorsys.keymanagement.api.generator.SigningKeyGenerator;
import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.KeyPairData;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.KeyPairGenerator;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.V3CertificateUtils;

import javax.inject.Inject;
import java.security.Provider;

public class DefaultSigningKeyGeneratorImpl implements SigningKeyGenerator {

    private final Provider provider;

    @Inject
    public DefaultSigningKeyGeneratorImpl(Provider provider) {
        this.provider = provider;
    }

    @Override
    public KeyPairEntry generate(Signing fromTemplate) {
        KeyPairData keyPair = KeyPairGenerator.builder()
                .provider(provider)
                .keyAlgo(fromTemplate.getAlgo())
                .keySize(fromTemplate.getSize())
                .serverSigAlgo(fromTemplate.getSigAlgo())
                .serverKeyPairName(fromTemplate.getCommonName())
                .build()
                .generateSignatureKey();

        return KeyPairEntry.builder()
                .pair(keyPair.getKeyPair().getKeyPair())
                .certificate(V3CertificateUtils.getX509JavaCertificate(provider, keyPair.getKeyPair().getSubjectCert()))
                .build();
    }
}
