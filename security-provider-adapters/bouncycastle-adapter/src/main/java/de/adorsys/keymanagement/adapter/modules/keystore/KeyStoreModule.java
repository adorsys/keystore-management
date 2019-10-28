package de.adorsys.keymanagement.adapter.modules.keystore;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.bouncycastle.adapter.services.persist.DefaultKeyStoreOperImpl;
import de.adorsys.keymanagement.api.types.KeyStoreConfig;

@Module
public abstract class KeyStoreModule {

    @Provides
    static KeyStoreConfig keyStoreConfig() {
        return KeyStoreConfig.builder().build();
    }

    @Binds
    abstract KeyStoreCreator creator(DefaultKeyStoreOperImpl oper);
}
