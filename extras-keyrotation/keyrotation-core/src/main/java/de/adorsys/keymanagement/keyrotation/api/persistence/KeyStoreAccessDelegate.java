package de.adorsys.keymanagement.keyrotation.api.persistence;

import java.security.KeyStore;

/**
 * This interface intercepts calls to {@link KeyStoreAccess} allowing to cache KeyStore in memory instead
 * of deserialization on each request.
 */
public interface KeyStoreAccessDelegate {

    /**
     * Must return null if KeyStore is absent.
     * @param original Underlying class that can perform the operation
     */
    KeyStore read(KeyStoreAccess original);

    /**
     * @param original Underlying class that can perform the operation
     * @param keyStore KeyStore to save
     */
    void write(KeyStoreAccess original, KeyStore keyStore);

    // TODO juggler.readKeys().fromKeyStore can be cached too.
}
