package de.adorsys.keymanagement.juggler.modules.generator;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.core.generator.DefaultKeyGeneratorImpl;

@Module
public abstract class GeneratorModule {

    @Binds
    abstract KeyGenerator keyGenerator(DefaultKeyGeneratorImpl keyGenerator);
}
