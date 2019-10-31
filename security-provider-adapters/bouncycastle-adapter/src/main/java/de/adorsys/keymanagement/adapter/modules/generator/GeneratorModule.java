package de.adorsys.keymanagement.adapter.modules.generator;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import de.adorsys.keymanagement.api.generator.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.generator.SigningKeyGenerator;
import de.adorsys.keymanagement.bouncycastle.adapter.services.generator.DefaultEncryptingKeyGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.services.generator.DefaultSecretKeyGeneratorImpl;
import de.adorsys.keymanagement.bouncycastle.adapter.services.generator.DefaultSigningKeyGeneratorImpl;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

@Module
public abstract class GeneratorModule {

    @Provides
    static Provider provider() {
        if (null == Security.getProperty(BouncyCastleProvider.PROVIDER_NAME)) {
            Security.addProvider(new BouncyCastleProvider());
        }

        return Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
    }

    @Binds
    abstract EncryptingKeyGenerator encryptingKeyGenerator(DefaultEncryptingKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SecretKeyGenerator secretKeyGenerator(DefaultSecretKeyGeneratorImpl keyGenerator);

    @Binds
    abstract SigningKeyGenerator signingKeyGenerator(DefaultSigningKeyGeneratorImpl keyGenerator);
}
