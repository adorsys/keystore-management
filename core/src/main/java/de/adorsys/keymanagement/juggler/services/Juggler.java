package de.adorsys.keymanagement.juggler.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.api.persist.KeyMetadataPersister;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.api.source.KeyMetadataExtractor;
import de.adorsys.keymanagement.api.source.KeyReader;
import de.adorsys.keymanagement.juggler.modules.generator.BCGeneratorModule;
import de.adorsys.keymanagement.juggler.modules.keystore.BCKeyStoreModule;
import de.adorsys.keymanagement.juggler.modules.persist.BCPersistModule;
import de.adorsys.keymanagement.juggler.modules.source.SourceModule;

import javax.annotation.Nullable;

@Component(modules = {
        BCGeneratorModule.class,
        BCKeyStoreModule.class,
        BCPersistModule.class,
        SourceModule.class
})
public interface Juggler {

    KeyGenerator generateKeys();
    KeyStoreCreator toKeystore();
    KeyReader readKeys();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder metadataExtractor(@Nullable KeyMetadataExtractor extractor);

        @BindsInstance
        Builder metadataPersister(@Nullable KeyMetadataPersister persister);

        Juggler build();
    }
}
