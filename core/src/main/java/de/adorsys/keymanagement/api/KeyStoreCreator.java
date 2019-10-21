package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.types.source.KeySet;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface KeyStoreCreator {

    KeyStore generate(KeySet keySet);
    KeyStore generate(KeySet keySet, Supplier<char[]> defaultKeyPassword);
}
