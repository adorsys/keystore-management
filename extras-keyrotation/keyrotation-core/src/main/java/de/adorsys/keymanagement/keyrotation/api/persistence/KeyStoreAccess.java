package de.adorsys.keymanagement.keyrotation.api.persistence;

import java.security.KeyStore;

/**
 * Higher level KeyStore access than bytes.
 */
public interface KeyStoreAccess {

    /**
     * Must return null if KeyStore is absent.
     */
    KeyStore read();
    void write(KeyStore keyStore);

    // TODO juggler.readKeys().fromKeyStore can be cached too.
}
