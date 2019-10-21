package de.adorsys.keymanagement.bouncycastle.adapter.modules.metadata;

import dagger.Module;
import dagger.Provides;
import de.adorsys.keymanagement.api.metadata.KeyMetadataOper;
import de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence;
import de.adorsys.keymanagement.api.metadata.NoOpMetadataPersistence;
import de.adorsys.keymanagement.bouncycastle.adapter.services.metadata.BCPersister;
import de.adorsys.keymanagement.bouncycastle.adapter.services.metadata.DefaultBCMetadataPersister;

import javax.annotation.Nullable;

@Module
public class BCMetadataModule {

    @Provides
    KeyMetadataOper metadataOper(@Nullable KeyMetadataPersistence userProvided, DefaultBCMetadataPersister persistor) {
        if (null == userProvided) {
            return new NoOpMetadataPersistence();
        }

        if (userProvided instanceof BCPersister) {
            return persistor;
        }

        return userProvided;
    }
}
