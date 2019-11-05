package de.adorsys.keymanagement.keyrotation.impl.services;

import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStoreAccess;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;

import javax.inject.Inject;
import java.security.KeyStore;

public class KeyStoreAccessImpl implements KeyStoreAccess {

    private final KeyStorePersistence persistence;
    private final Juggler juggler;
    private final KeyRotationConfig config;

    @Inject
    public KeyStoreAccessImpl(KeyStorePersistence persistence, Juggler juggler, KeyRotationConfig config) {
        this.persistence = persistence;
        this.juggler = juggler;
        this.config = config;
    }

    @Override
    public KeyStore read() {
        if (null == persistence.read()) {
            return null;
        }

        return juggler.serializeDeserialize().deserialize(
                persistence.read(), config.keyStorePassword()
        );
    }

    @Override
    public void write(KeyStore keyStore) {
        persistence.write(
                juggler.serializeDeserialize().serialize(keyStore, config.keyStorePassword())
        );
    }
}
