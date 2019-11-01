package de.adorsys.keymanagement.keyrotation.modules;

import dagger.Binds;
import dagger.Module;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyGenerator;
import de.adorsys.keymanagement.keyrotation.api.services.Rotation;
import de.adorsys.keymanagement.keyrotation.impl.services.KeyGeneratorImpl;
import de.adorsys.keymanagement.keyrotation.impl.services.RotationImpl;

@Module
public abstract class RotationModule {

    @Binds
    abstract Rotation rotation(RotationImpl rotation);

    @Binds
    abstract KeyGenerator generator(KeyGeneratorImpl rotation);
}
