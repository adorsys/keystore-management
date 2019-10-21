package de.adorsys.keymanagement.juggler.modules.persist;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.keystore.KeyStoreOper;
import de.adorsys.keymanagement.persist.DefaultKeyStoreOperImpl;

@Module
public abstract class PersistModule {

    @Binds
    abstract KeyStoreOper keyStoreOper(DefaultKeyStoreOperImpl oper);
}
