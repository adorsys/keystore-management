package de.adorsys.keymanagement.bouncycastle.adapter.services.generator;

import de.adorsys.keymanagement.api.generator.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.KeyPairData;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.KeyPairGenerator;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.V3CertificateUtils;

import javax.inject.Inject;
import java.security.Provider;

public class DefaultEncryptingKeyGeneratorImpl implements EncryptingKeyGenerator {

    private final Provider provider;

    @Inject
    public DefaultEncryptingKeyGeneratorImpl(Provider provider) {
        this.provider = provider;
    }

    @Override
    public KeyPairEntry generate(Encrypting fromTemplate) {
        KeyPairData keyPair = KeyPairGenerator.builder()
                .provider(provider)
                .keyAlgo(fromTemplate.getAlgo())
                .keySize(fromTemplate.getSize())
                .serverSigAlgo(fromTemplate.getSigAlgo())
                .serverKeyPairName(fromTemplate.getCommonName())
                .paramSpec(fromTemplate.getParamSpec())
                .build()
                .generateEncryptionKey();

        return KeyPairEntry.builder()
                .pair(keyPair.getKeyPair().getKeyPair())
                .certificate(V3CertificateUtils.getX509JavaCertificate(provider, keyPair.getKeyPair().getSubjectCert()))
                .build();
    }
}
