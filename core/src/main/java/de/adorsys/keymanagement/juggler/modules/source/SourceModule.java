package de.adorsys.keymanagement.juggler.modules.source;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.KeyReader;
import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.core.source.DefaultKeyReaderImpl;
import de.adorsys.keymanagement.core.source.DefaultKeyStoreSourceImpl;

@Module
public abstract class SourceModule {

    @Binds
    abstract KeySource keySource(DefaultKeyStoreSourceImpl source);

    @Binds
    abstract KeyReader keyReader(DefaultKeyReaderImpl source);
}
