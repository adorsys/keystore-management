package de.adorsys.keymanagement.keyrotation.modules;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.core.metadata.MetadataPersistenceConfig;
import de.adorsys.keymanagement.core.metadata.WithPersister;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import de.adorsys.keymanagement.keyrotation.api.services.KeyGenerator;
import de.adorsys.keymanagement.keyrotation.api.services.KeyStoreManager;
import de.adorsys.keymanagement.keyrotation.api.services.Rotation;
import de.adorsys.keymanagement.keyrotation.api.types.KeyState;
import de.adorsys.keymanagement.keyrotation.impl.services.KeyGeneratorImpl;
import de.adorsys.keymanagement.keyrotation.impl.services.RotationImpl;

import javax.annotation.Nullable;

@Module
public abstract class RotationModule {

    @Binds
    abstract Rotation rotation(RotationImpl rotation);

    @Binds
    abstract KeyGenerator generator(KeyGeneratorImpl rotation);

    @Provides
    static Juggler keyStoreManagement(@Nullable KeyStoreManager manager) {
        if (null != manager) {
            return manager;
        }

        return DaggerBCJuggler.builder()
                .metadataConfig(
                        MetadataPersistenceConfig.builder()
                                .metadataClass(KeyState.class)
                                .build()
                )
                .metadataPersister(new WithPersister())
                .build();
    }
}
