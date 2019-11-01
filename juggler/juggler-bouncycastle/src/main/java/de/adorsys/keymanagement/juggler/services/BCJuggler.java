package de.adorsys.keymanagement.juggler.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.adapter.modules.generator.GeneratorModule;
import de.adorsys.keymanagement.adapter.modules.keystore.KeyStoreModule;
import de.adorsys.keymanagement.adapter.modules.persist.PersistModule;
import de.adorsys.keymanagement.adapter.modules.serde.SerdeModule;
import de.adorsys.keymanagement.adapter.modules.source.DecodeModule;
import de.adorsys.keymanagement.api.KeyStoreManager;
import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence;
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
        DecodeModule.class,
        SerdeModule.class,
})
public interface BCJuggler extends KeyStoreManager {

    @Component.Builder
    interface Builder {

        /**
         * Tells which metadata class to persist within {@link java.security.KeyStore} and how to
         * serialize/deserialize it.
         */
        @BindsInstance
        Builder metadataConfig(@Nullable MetadataPersistenceConfig config);

        /**
         * Configures default keystore type and encryption, can be overridden by withConfig method.
         */
        @BindsInstance
        Builder keyStoreConfig(@Nullable KeyStoreConfig config);

        /**
         * Tells how to persist class provided by {@link MetadataPersistenceConfig}.
         * Most probably you want to pass {@link de.adorsys.keymanagement.core.metadata.WithPersister} class here.
         */
        @BindsInstance
        Builder metadataPersister(@Nullable KeyMetadataPersistence persistence);

        BCJuggler build();
    }
}
