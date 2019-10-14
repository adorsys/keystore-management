package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.source.KeySet;

import java.security.KeyStore;

public interface KeyStoreCreator {

    KeyStore generate(KeySet keySet);
}
