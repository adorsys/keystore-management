package de.adorsys.keymanagement.adapter.modules.source;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.api.source.KeyDecoder;
import de.adorsys.keymanagement.bouncycastle.adapter.services.decode.DefaultDecoderImpl;

@Module
public abstract class DecodeModule {

    @Binds
    abstract KeyDecoder decoder(DefaultDecoderImpl decoder);
}
