package de.adorsys.keymanagement.api.persist;

import de.adorsys.keymanagement.api.types.source.KeySet;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface KeyStoreCreator {

    KeyStore generate(KeySet keySet);
    KeyStore generate(KeySet keySet, Supplier<char[]> defaultKeyPassword);

    KeyStore generateWithoutMetadata(KeySet keySet);
    KeyStore generateWithoutMetadata(KeySet keySet, Supplier<char[]> defaultKeyPassword);
}
