package de.adorsys.keymanagement.bouncycastle.adapter.services.generator;

import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.types.template.generated.Pbe;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.SecretKeyBuilder;
import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.security.Provider;
import java.security.SecureRandom;

public class DefaultSecretKeyGeneratorImpl implements SecretKeyGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Provider provider;

    @Inject
    public DefaultSecretKeyGeneratorImpl(Provider provider) {
        this.provider = provider;
    }

    @Override
    public ProvidedKey generate(Pbe fromTemplate) {
        return ProvidedKey.builder()
                .keyTemplate(fromTemplate)
                .metadata(fromTemplate.getMetadata())
                .key(generateSecret(fromTemplate))
                .build();
    }

    @Override
    public ProvidedKey generateRaw(Pbe fromTemplate) {
        return ProvidedKey.builder()
                .keyTemplate(fromTemplate)
                .metadata(fromTemplate.getMetadata())
                .key(generateRawSecret(fromTemplate))
                .build();
    }

    @Override
    public ProvidedKey generate(Secret fromTemplate) {
        return ProvidedKey.builder()
                .keyTemplate(fromTemplate)
                .metadata(fromTemplate.getMetadata())
                .key(generateSecret(fromTemplate))
                .build();
    }

    private SecretKey generateSecret(Secret secret) {
        return new SecretKeyBuilder()
                .withProvider(provider)
                .withKeyAlg(secret.getAlgo())
                .withKeyLength(secret.getSize())
                .build();
    }

    @SneakyThrows
    private SecretKey generateSecret(Pbe secret) {
        byte[] salt = new byte[secret.getSaltLen()];
        SECURE_RANDOM.nextBytes(salt);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(
                secret.getData(),
                salt,
                secret.getIterCount()
        );

        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(secret.getAlgo(), provider);
        return keyFac.generateSecret(pbeKeySpec);
    }

    @SneakyThrows
    private SecretKey generateRawSecret(Pbe secret) {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(secret.getData());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(secret.getAlgo(), provider);
        return keyFac.generateSecret(pbeKeySpec);
    }
}
