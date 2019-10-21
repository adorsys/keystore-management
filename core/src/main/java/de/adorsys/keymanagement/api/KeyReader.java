package de.adorsys.keymanagement.api;

import java.security.KeyStore;
import java.util.function.Function;

public interface KeyReader {

    KeyStoreView fromKeyStore(KeyStore keyStore, Function<String, char[]> keyPassword);
}
