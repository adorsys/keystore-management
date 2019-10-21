package de.adorsys.keymanagement.api.source;

import de.adorsys.keymanagement.api.keystore.KeyStoreView;

import java.security.KeyStore;
import java.util.function.Function;

public interface KeyReader {

    KeyStoreView fromKeyStore(KeyStore keyStore, Function<String, char[]> keyPassword);
}
