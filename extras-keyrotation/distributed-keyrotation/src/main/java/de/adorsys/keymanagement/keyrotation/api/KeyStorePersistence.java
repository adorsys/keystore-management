package de.adorsys.keymanagement.keyrotation.api;

import java.security.KeyStore;

public interface KeyStorePersistence {

    KeyStore read();
    void write(KeyStore keyStore);
}
