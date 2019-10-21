package de.adorsys.keymanagement.juggler.modules.generator;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.generator.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.generator.SigningKeyGenerator;
import de.adorsys.keymanagement.core.generator.DefaultKeyGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.generator.DefaultEncryptingKeyGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.generator.DefaultSecretKeyGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.generator.DefaultSigningKeyGeneratorImpl;

@Module
public abstract class BCGeneratorModule {

    @Binds
    abstract KeyGenerator keyGenerator(DefaultKeyGeneratorImpl keyGenerator);

    @Binds
    abstract EncryptingKeyGenerator encryptingKeyGenerator(DefaultEncryptingKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SecretKeyGenerator secretKeyGenerator(DefaultSecretKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SigningKeyGenerator signingKeyGenerator(DefaultSigningKeyGeneratorImpl keyGenerator);
}
