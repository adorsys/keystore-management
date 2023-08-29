package de.adorsys.keymanagement.keyrotation.api.persistence;

import javax.annotation.Nullable;

public interface KeyStorePersistence {

    /**
     * Reads KeyStore bytes from persistent storage, should return null if KeyStore does not exist.
     */
    @Nullable
    byte[] read();

    /**
     * Writes KeyStore bytes to persistent storage.
     */
    void write(byte[] keyStore);
}
