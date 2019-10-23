package de.adorsys.keymanagement.juggler.modules.generator;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.core.generator.TemplateMappingGeneratorImpl;

@Module
public abstract class TemplateMappingModule {

    @Binds
    abstract KeyGenerator keyGenerator(TemplateMappingGeneratorImpl keyGenerator);
}
