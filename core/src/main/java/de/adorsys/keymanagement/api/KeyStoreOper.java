package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.source.KeySet;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;

import java.security.KeyStore;

public interface KeyStoreOper {

    KeyStore generate(KeySet keySet);
    String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyTemplate entry);
}
