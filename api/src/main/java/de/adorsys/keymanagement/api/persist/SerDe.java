package de.adorsys.keymanagement.api.persist;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface SerDe {

    byte[] serialize(KeyStore keyStore, Supplier<char[]> keyStorePassword);
    KeyStore deserialize(byte[] keyStore, Supplier<char[]> keyStorePassword);
}
