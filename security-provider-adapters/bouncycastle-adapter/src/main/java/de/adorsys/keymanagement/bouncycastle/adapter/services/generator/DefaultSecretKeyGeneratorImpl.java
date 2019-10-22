package de.adorsys.keymanagement.bouncycastle.adapter.services.generator;

import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.SecretKeyData;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.keystore.ReadKeyPassword;
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
        return generateSecret(fromTemplate).getSecretKey();
    }

    @Override
    public ProvidedKey generate(Secret fromTemplate) {
        return ProvidedKey.builder()
                .keyTemplate(fromTemplate)
                .key(generateSecret(fromTemplate).getSecretKey())
                .build();
    }

    @Override
    @SneakyThrows
    public SecretKey generateFromPassword(Supplier<char[]> password) {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.get());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_256"); // FIXME configurable
        return keyFac.generateSecret(pbeKeySpec);
    }

    private SecretKeyData generateSecret(Secret secret) {
        return new de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator.SecretKeyGeneratorImpl(
                secret.getAlgo(),
                secret.getSize()
        )
                .generate(
                        "STUB",
                        new ReadKeyPassword("STUB") // FIXME unneeded
                );
    }
}
