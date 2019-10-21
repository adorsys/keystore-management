package de.adorsys.keymanagement.juggler.modules.generator;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.KeyGenerator;
import de.adorsys.keymanagement.api.SecretKeyGenerator;
import de.adorsys.keymanagement.api.SigningKeyGenerator;
import de.adorsys.keymanagement.core.generator.DefaultEncryptingKeyGeneratorImpl;
import de.adorsys.keymanagement.core.generator.DefaultKeyGeneratorImpl;
import de.adorsys.keymanagement.core.generator.DefaultSecretKeyGeneratorImpl;
import de.adorsys.keymanagement.core.generator.DefaultSigningKeyGeneratorImpl;

@Module
public abstract class GeneratorModule {

    @Binds
    abstract KeyGenerator keyGenerator(DefaultKeyGeneratorImpl keyGenerator);

    @Binds
    abstract EncryptingKeyGenerator encryptingKeyGenerator(DefaultEncryptingKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SecretKeyGenerator secretKeyGenerator(DefaultSecretKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SigningKeyGenerator signingKeyGenerator(DefaultSigningKeyGeneratorImpl keyGenerator);
}
