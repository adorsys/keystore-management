package de.adorsys.keymanagement.keyrotation.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.keyrotation.api.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.KeyView;
import de.adorsys.keymanagement.keyrotation.api.Rotation;
import de.adorsys.keymanagement.keyrotation.api.RotationLocker;

import javax.annotation.Nullable;
import java.security.Key;
import java.time.Clock;
import java.util.function.Function;
import java.util.function.Supplier;

@Component(modules = {})
public interface RotatedKeyStore {

    Rotation rotation();
    Key keyById(String keyId);
    KeyView keys();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder persistence(KeyStorePersistence persistence);

        @BindsInstance
        Builder distributedLock(RotationLocker locker);

        @BindsInstance
        Builder timeSource(@Nullable Clock timeSource);

        RotatedKeyStore build();
    }
}
