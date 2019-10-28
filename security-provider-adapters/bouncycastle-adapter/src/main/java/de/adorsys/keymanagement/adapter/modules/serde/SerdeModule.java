package de.adorsys.keymanagement.adapter.modules.serde;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.persist.SerDe;
import de.adorsys.keymanagement.bouncycastle.adapter.services.serde.DefaultKeyStoreSerde;

@Module
public abstract class SerdeModule {

    @Binds
    abstract SerDe serializeDeserialize(DefaultKeyStoreSerde serde);
}
