package de.adorsys.keymanagement.adapter.modules.keystore;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.bouncycastle.adapter.services.persist.DefaultKeyStoreOperImpl;

@Module
public abstract class KeyStoreModule {

    @Binds
    abstract KeyStoreCreator creator(DefaultKeyStoreOperImpl oper);
}
