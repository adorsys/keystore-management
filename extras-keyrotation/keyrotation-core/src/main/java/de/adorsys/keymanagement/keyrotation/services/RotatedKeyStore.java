package de.adorsys.keymanagement.keyrotation.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import de.adorsys.keymanagement.keyrotation.api.services.KeyStoreManager;
import de.adorsys.keymanagement.keyrotation.api.services.KeyView;
import de.adorsys.keymanagement.keyrotation.api.services.Rotation;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.modules.RotationModule;
import de.adorsys.keymanagement.keyrotation.modules.ViewModule;

import javax.annotation.Nullable;
import java.time.Clock;

@Component(modules = {
        RotationModule.class,
        ViewModule.class
})
public interface RotatedKeyStore {

    Rotation rotation();
    KeyView keys();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder keyStoreManager(@Nullable KeyStoreManager juggler);

        @BindsInstance
        Builder timeSource(@Nullable Clock timeSource);

        @BindsInstance
        Builder persistence(KeyStorePersistence persistence);

        @BindsInstance
        Builder distributedLock(RotationLocker locker);

        @BindsInstance
        Builder rotationConfig(KeyRotationConfig config);

        RotatedKeyStore build();
    }
}
