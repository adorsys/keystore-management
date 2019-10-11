package de.adorsys.keymanagement.juggler;

import dagger.Component;
import de.adorsys.keymanagement.api.KeyGenerator;
import de.adorsys.keymanagement.api.KeyStoreCreator;
import de.adorsys.keymanagement.core.source.KeySource;
import de.adorsys.keymanagement.juggler.generator.GeneratorModule;
import de.adorsys.keymanagement.juggler.keystore.KeyStoreModule;
import de.adorsys.keymanagement.juggler.persist.PersistModule;
import de.adorsys.keymanagement.juggler.source.SourceModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        GeneratorModule.class,
        KeyStoreModule.class,
        PersistModule.class,
        SourceModule.class
})
public interface Juggler {

    KeyGenerator generate();
    KeyStoreCreator keystore();
    KeySource source();
}
