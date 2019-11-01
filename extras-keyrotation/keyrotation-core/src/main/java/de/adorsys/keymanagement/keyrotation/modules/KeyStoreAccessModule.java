package de.adorsys.keymanagement.keyrotation.modules;

import dagger.Module;
import dagger.Provides;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStoreAccess;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStoreAccessDelegate;
import de.adorsys.keymanagement.keyrotation.impl.services.KeyStoreAccessImpl;

import javax.annotation.Nullable;
import java.security.KeyStore;

/**
 * This module can cache KeyStore for example.
 */
@Module
public abstract class KeyStoreAccessModule {

    @Provides
    static KeyStoreAccess accessCaptor(@Nullable KeyStoreAccessDelegate callCaptor, KeyStoreAccessImpl access) {
        if (null != callCaptor) {
            // Delegates work to KeyStoreAccessDelegate implementor:
            return new KeyStoreAccess() {

                @Override
                public KeyStore read() {
                    return callCaptor.read(access);
                }

                @Override
                public void write(KeyStore keyStore) {
                    callCaptor.write(access, keyStore);
                }
            };
        }

        return access;
    }
}
