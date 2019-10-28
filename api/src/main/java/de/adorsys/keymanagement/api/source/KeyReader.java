package de.adorsys.keymanagement.api.source;

import de.adorsys.keymanagement.api.keystore.KeyStoreView;

import java.security.KeyStore;
import java.util.function.Function;

public interface KeyReader {

    /**
     * Creates key view that supports adding/removing/updating key entries through it. All updating operations
     * will be propagated to underlying {@link java.security.KeyStore}.
     * @param keyStore KeyStore from which to extract keys and create view.
     * @param keyPassword Per alias key password provider (key alias - its password).
     * @return Modifiable KeyStore view.
     */
    KeyStoreView fromKeyStore(KeyStore keyStore, Function<String, char[]> keyPassword);
}
