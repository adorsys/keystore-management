package de.adorsys.keymanagement.juggler.modules.keystore;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.persist.DefaultKeyStoreOperImpl;

@Module
public abstract class KeyStoreModule {

    @Binds
    abstract KeyStoreCreator creator(DefaultKeyStoreOperImpl oper);
}
