package de.adorsys.keymanagement.juggler.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.adapter.modules.generator.GeneratorModule;
import de.adorsys.keymanagement.adapter.modules.keystore.KeyStoreModule;
import de.adorsys.keymanagement.adapter.modules.persist.PersistModule;
import de.adorsys.keymanagement.adapter.modules.source.DecodeModule;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.api.source.KeyDecoder;
import de.adorsys.keymanagement.api.source.KeyReader;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.juggler.modules.generator.TemplateMappingModule;
import de.adorsys.keymanagement.juggler.modules.metadata.MetadataModule;
import de.adorsys.keymanagement.juggler.modules.source.SourceModule;

import javax.annotation.Nullable;

@Component(modules = {
        TemplateMappingModule.class,
        GeneratorModule.class,
        KeyStoreModule.class,
        PersistModule.class,
        MetadataModule.class,
        SourceModule.class,
        DecodeModule.class
})
public interface Juggler {

    KeyGenerator generateKeys();
    KeyStoreCreator toKeystore();
    KeyReader readKeys();
    KeyDecoder decode();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder metadataConfig(@Nullable MetadataPersistenceConfig config);

        @BindsInstance
        Builder metadataPersister(@Nullable KeyMetadataPersistence persistence);

        Juggler build();
    }
}
