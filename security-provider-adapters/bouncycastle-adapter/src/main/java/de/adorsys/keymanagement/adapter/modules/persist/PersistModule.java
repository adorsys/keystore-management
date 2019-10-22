package de.adorsys.keymanagement.adapter.modules.persist;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.keystore.KeyStoreOper;
import de.adorsys.keymanagement.bouncycastle.adapter.services.persist.DefaultKeyStoreOperImpl;

@Module
public abstract class PersistModule {

    @Binds
    abstract KeyStoreOper keyStoreOper(DefaultKeyStoreOperImpl oper);
}
