package de.adorsys.keymanagement.adapter.modules.generator;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.generator.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.generator.SigningKeyGenerator;
import de.adorsys.keymanagement.bouncycastle.adapter.services.generator.DefaultEncryptingKeyGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.services.generator.DefaultSecretKeyGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.services.generator.DefaultSigningKeyGeneratorImpl;

@Module
public abstract class GeneratorModule {

    @Binds
    abstract EncryptingKeyGenerator encryptingKeyGenerator(DefaultEncryptingKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SecretKeyGenerator secretKeyGenerator(DefaultSecretKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SigningKeyGenerator signingKeyGenerator(DefaultSigningKeyGeneratorImpl keyGenerator);
}
