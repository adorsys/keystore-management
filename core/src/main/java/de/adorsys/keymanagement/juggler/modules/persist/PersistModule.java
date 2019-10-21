package de.adorsys.keymanagement.juggler.modules.persist;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.core.persist.DefaultKeyStoreOperImpl;

@Module
public abstract class PersistModule {

    @Binds
    abstract KeyStoreOper keyStoreOper(DefaultKeyStoreOperImpl oper);
}
