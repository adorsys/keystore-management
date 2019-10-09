package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.KeySet;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;

import java.security.KeyStore;

public interface KeyStoreOper {

    KeyStore generate(KeySet keySet);
    String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyEntry entry);
    String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyPair pair);
    String addToKeyStoreAndGetName(KeyStore ks, Provided key);
}
