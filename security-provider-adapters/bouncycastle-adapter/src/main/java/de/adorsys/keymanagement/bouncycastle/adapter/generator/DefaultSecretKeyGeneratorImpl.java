package de.adorsys.keymanagement.bouncycastle.adapter.generator;

import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator.SecretKeyData;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore.ReadKeyPassword;

import javax.crypto.SecretKey;
import javax.inject.Inject;

public class DefaultSecretKeyGeneratorImpl implements SecretKeyGenerator {

    @Inject
    public DefaultSecretKeyGeneratorImpl() {
    }

    @Override
    public SecretKey generateKey(Secret fromTemplate) {
        return generateSecret(fromTemplate).getSecretKey();
    }

    @Override
    public ProvidedKey generate(Secret fromTemplate) {
        return ProvidedKey.builder()
                .keyTemplate(fromTemplate)
                .key(generateSecret(fromTemplate).getSecretKey())
                .build();
    }

    private SecretKeyData generateSecret(Secret secret) {
        return new de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator.SecretKeyGeneratorImpl(
                secret.getAlgo(),
                secret.getSize()
        )
                .generate(
                        "STUB",
                        new ReadKeyPassword("STUB") // FIXME unneeded
                );
    }
}
