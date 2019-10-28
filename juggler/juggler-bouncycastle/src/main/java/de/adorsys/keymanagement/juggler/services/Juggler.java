package de.adorsys.keymanagement.juggler.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.adapter.modules.generator.GeneratorModule;
import de.adorsys.keymanagement.adapter.modules.keystore.KeyStoreModule;
import de.adorsys.keymanagement.adapter.modules.persist.PersistModule;
import de.adorsys.keymanagement.adapter.modules.serde.SerdeModule;
import de.adorsys.keymanagement.adapter.modules.source.DecodeModule;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.api.persist.SerDe;
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
        DecodeModule.class,
        SerdeModule.class,
})
public interface Juggler {

    /**
     * Generates keys or key sets.
     */
    KeyGenerator generateKeys();

    /**
     * Creates {@link java.security.KeyStore} from key set.
     */
    KeyStoreCreator toKeystore();

    /**
     * Creates key views from different sources, for example from {@link java.security.KeyStore}.
     * Typically, these key views allows you to perform complicated queries and to add/or remove keys.
     */
    KeyReader readKeys();

    /**
     * Decodes keys from their encoded byte representation. Mostly used for PBE keys that are stored as is
     * in KeyStore.
     */
    KeyDecoder decode();

    /**
     * Helper to serialize and deserialize {@link java.security.KeyStore} to and from byte[].
     */
    SerDe serializeDeserialize();

    @Component.Builder
    interface Builder {

        /**
         * Tells which metadata class to persist within {@link java.security.KeyStore} and how to
         * serialize/deserialize it.
         */
        @BindsInstance
        Builder metadataConfig(@Nullable MetadataPersistenceConfig config);

        /**
         * Tells how to persist class provided by {@link MetadataPersistenceConfig}.
         * Most probably you want to pass {@link de.adorsys.keymanagement.core.metadata.WithPersister} class here.
         */
        @BindsInstance
        Builder metadataPersister(@Nullable KeyMetadataPersistence persistence);

        Juggler build();
    }
}
