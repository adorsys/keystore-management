package de.adorsys.keymanagement.keyrotation.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.api.KeyStoreManager;
import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStoreAccessDelegate;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import de.adorsys.keymanagement.keyrotation.api.services.KeyView;
import de.adorsys.keymanagement.keyrotation.api.services.Rotation;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.modules.KeyStoreAccessModule;
import de.adorsys.keymanagement.keyrotation.modules.RotationModule;
import de.adorsys.keymanagement.keyrotation.modules.ViewModule;

import javax.annotation.Nullable;
import java.time.Clock;

@Component(modules = {
        KeyStoreAccessModule.class,
        RotationModule.class,
        ViewModule.class
})
public interface RotatedKeyStore {

    Rotation rotation();
    KeyView keys();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder keyStoreConfig(@Nullable KeyStoreConfig manager);

        @BindsInstance
        Builder keyStoreManager(@Nullable KeyStoreManager manager);

        @BindsInstance
        Builder timeSource(@Nullable Clock timeSource);

        /**
         * Allows user to provide in-memory keystore cache.
         */
        @BindsInstance
        Builder accessDelegate(@Nullable KeyStoreAccessDelegate callCaptor);

        @BindsInstance
        Builder persistence(KeyStorePersistence persistence);

        @BindsInstance
        Builder distributedLock(RotationLocker locker);

        @BindsInstance
        Builder rotationConfig(KeyRotationConfig config);

        RotatedKeyStore build();
    }
}
