package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.types.template.ProvidedKeyTemplate;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface KeyStoreOper extends KeyStoreCreator {

    String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyTemplate entry, Supplier<char[]> defaultKeyPassword);
}
