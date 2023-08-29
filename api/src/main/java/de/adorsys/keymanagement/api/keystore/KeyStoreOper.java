package de.adorsys.keymanagement.api.keystore;

import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface KeyStoreOper extends KeyStoreCreator {

    /**
     * Adds key entry to keystore.
     * @param ks keystore where to add key
     * @param entry Key that is going to be added to KeyStore. Note that ProvidedKeyTemplate is the wrapper
     *              for KeyStore entries that provides additional information like key name
     * @param defaultKeyPassword default key password used when password inside entry is null
     * @return generated alias of added to keystore entry.
     */
    String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyTemplate entry, Supplier<char[]> defaultKeyPassword);
}
