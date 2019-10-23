package de.adorsys.keymanagement.bouncycastle.adapter.services.generator;

import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.SecretKeyBuilder;
import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.util.function.Supplier;

public class DefaultSecretKeyGeneratorImpl implements SecretKeyGenerator {

    @Inject
    public DefaultSecretKeyGeneratorImpl() {
    }

    @Override
    public SecretKey generateKey(Secret fromTemplate) {
        return generateSecret(fromTemplate);
    }

    @Override
    public ProvidedKey generate(Secret fromTemplate) {
        return ProvidedKey.builder()
                .keyTemplate(fromTemplate)
                .key(generateSecret(fromTemplate))
                .build();
    }

    @Override
    @SneakyThrows
    public SecretKey generateFromPassword(Supplier<char[]> password) {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.get());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256"); // FIXME configurable
        return keyFac.generateSecret(pbeKeySpec);
    }

    private SecretKey generateSecret(Secret secret) {
        return new SecretKeyBuilder()
                .withKeyAlg(secret.getAlgo())
                .withKeyLength(secret.getSize())
                .build();
    }
}
