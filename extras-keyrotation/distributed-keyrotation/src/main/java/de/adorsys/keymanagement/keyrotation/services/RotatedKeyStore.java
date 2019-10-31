package de.adorsys.keymanagement.keyrotation.services;

import dagger.BindsInstance;
import dagger.Component;
import de.adorsys.keymanagement.keyrotation.api.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.KeyType;
import de.adorsys.keymanagement.keyrotation.api.RotationLocker;

import java.security.Key;

@Component(modules = {})
public interface RotatedKeyStore {

    void rotateKeys();
    Key keyById(String keyId);
    Key validKeyForUsage(KeyType forType);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder persistence(KeyStorePersistence persistence);

        @BindsInstance
        Builder distributedLock(RotationLocker locker);

        RotatedKeyStore build();
    }
}
