package de.adorsys.keymanagement.core.impl;

import de.adorsys.keymanagement.api.SecretKeyGenerator;
import de.adorsys.keymanagement.core.deprecated.generator.SecretKeyData;
import de.adorsys.keymanagement.core.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.provided.Provided;

import javax.crypto.SecretKey;

public class SecretKeyGeneratorImpl implements SecretKeyGenerator {

    @Override
    public SecretKey generateKey(Secret fromTemplate) {
        return generateSecret(fromTemplate).getSecretKey();
    }

    @Override
    public Provided generate(Secret fromTemplate) {
        return Provided.builder()
                .keyTemplate(fromTemplate)
                .key(generateSecret(fromTemplate).getSecretKey())
                .build();
    }

    private SecretKeyData generateSecret(Secret secret) {
        return new de.adorsys.keymanagement.core.deprecated.generator.SecretKeyGeneratorImpl(
                secret.getAlgo(),
                secret.getSize()
        )
                .generate(
                        secret.getName(),
                        new ReadKeyPassword("STUB") // FIXME unneeded
                );
    }
}
