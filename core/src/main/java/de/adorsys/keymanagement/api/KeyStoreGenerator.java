package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.KeySet;

import java.security.KeyStore;

public interface KeyStoreGenerator {

    KeyStore generate(KeySet keySet);
}
