package de.adorsys.keymanagement.juggler.modules.keystore;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.bouncycastle.adapter.persist.DefaultKeyStoreOperImpl;

@Module
public abstract class BCKeyStoreModule {

    @Binds
    abstract KeyStoreCreator creator(DefaultKeyStoreOperImpl oper);
}
