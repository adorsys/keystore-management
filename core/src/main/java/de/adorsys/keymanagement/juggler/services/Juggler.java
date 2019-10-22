package de.adorsys.keymanagement.juggler.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.api.source.KeyReader;
import de.adorsys.keymanagement.bouncycastle.adapter.modules.persist.BCPersistModule;
import de.adorsys.keymanagement.bouncycastle.adapter.modules.generator.BCGeneratorModule;
import de.adorsys.keymanagement.bouncycastle.adapter.modules.keystore.BCKeyStoreModule;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.juggler.modules.metadata.MetadataModule;
import de.adorsys.keymanagement.juggler.modules.source.SourceModule;
import de.adorsys.keymanagement.juggler.modules.generator.GeneratorModule;

import javax.annotation.Nullable;

@Component(modules = {
        GeneratorModule.class,
        BCGeneratorModule.class, // FIXME BC* these should be package independent and provided by security-prov-adapter
        BCKeyStoreModule.class,
        BCPersistModule.class,
        MetadataModule.class,
        SourceModule.class
})
public interface Juggler {

    KeyGenerator generateKeys();
    KeyStoreCreator toKeystore();
    KeyReader readKeys();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder withMetadataPersistenceConfig(@Nullable MetadataPersistenceConfig config);

        @BindsInstance
        Builder withMetadataPersister(@Nullable KeyMetadataPersistence persistence);

        Juggler build();
    }
}
