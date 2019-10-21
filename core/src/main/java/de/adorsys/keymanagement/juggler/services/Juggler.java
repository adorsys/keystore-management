package de.adorsys.keymanagement.juggler.services;

import dagger.Component;
import de.adorsys.keymanagement.api.KeyGenerator;
import de.adorsys.keymanagement.api.KeyReader;
import de.adorsys.keymanagement.api.KeyStoreCreator;
import de.adorsys.keymanagement.juggler.modules.generator.GeneratorModule;
import de.adorsys.keymanagement.juggler.modules.keystore.KeyStoreModule;
import de.adorsys.keymanagement.juggler.modules.persist.PersistModule;
import de.adorsys.keymanagement.juggler.modules.source.SourceModule;

@Component(modules = {
        GeneratorModule.class,
        KeyStoreModule.class,
        PersistModule.class,
        SourceModule.class
})
public interface Juggler {

    KeyGenerator generateKeys();
    KeyStoreCreator toKeystore();
    KeyReader readKeys();

    @Component.Builder
    interface Builder {
        Juggler build();
    }
}
