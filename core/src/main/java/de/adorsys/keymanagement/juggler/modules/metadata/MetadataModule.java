package de.adorsys.keymanagement.juggler.modules.metadata;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import de.adorsys.keymanagement.api.metadata.KeyMetadataOper;
import de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence;
import de.adorsys.keymanagement.api.metadata.NoOpMetadataPersistence;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.core.metadata.ToKeyStoreMetadataPersister;
import de.adorsys.keymanagement.core.metadata.WithPersister;

import javax.annotation.Nullable;

@Module
public class MetadataModule {

    @Provides
    KeyMetadataOper metadataOper(
            @Nullable MetadataPersistenceConfig config,
            @Nullable KeyMetadataPersistence userProvided,
            Lazy<ToKeyStoreMetadataPersister> persistor) {

        if (null == config) {
            return new NoOpMetadataPersistence();
        }

        if (null == userProvided) {
            return new NoOpMetadataPersistence();
        }

        if (userProvided instanceof WithPersister) {
            return persistor.get();
        }

        return userProvided;
    }
}
