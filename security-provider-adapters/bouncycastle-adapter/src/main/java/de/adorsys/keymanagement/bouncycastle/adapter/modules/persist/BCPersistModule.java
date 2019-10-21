package de.adorsys.keymanagement.bouncycastle.adapter.modules.persist;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.keystore.KeyStoreOper;
import de.adorsys.keymanagement.bouncycastle.adapter.persist.DefaultKeyStoreOperImpl;

@Module
public abstract class BCPersistModule {

    @Binds
    abstract KeyStoreOper keyStoreOper(DefaultKeyStoreOperImpl oper);
}
