package de.adorsys.keymanagement.api.keystore;

import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface KeyStoreOper extends KeyStoreCreator {

    String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyTemplate entry, Supplier<char[]> defaultKeyPassword);
}
