package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;

import java.security.KeyStore;

public interface KeyStoreOper extends KeyStoreCreator {

    String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyTemplate entry);
}
