package de.adorsys.keymanagement.keyrotation.api.persistence;

public interface KeyStorePersistence {

    byte[] read();
    void write(byte[] keyStore);
}
