package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.api.persist.KeyStoreCreator;
import de.adorsys.keymanagement.api.persist.SerDe;
import de.adorsys.keymanagement.api.source.KeyDecoder;
import de.adorsys.keymanagement.api.source.KeyReader;

/**
 * This interface provides all functions that are available in this library.
 */
public interface Juggler {

    /**
     * Generates keys or key sets.
     */
    KeyGenerator generateKeys();

    /**
     * Creates {@link java.security.KeyStore} from key set.
     */
    KeyStoreCreator toKeystore();

    /**
     * Creates key views from different sources, for example from {@link java.security.KeyStore}.
     * Typically, these key views allows you to perform complicated queries and to add/or remove keys.
     */
    KeyReader readKeys();

    /**
     * Decodes keys from their encoded byte representation. Mostly used for PBE keys that are stored as is
     * in KeyStore.
     */
    KeyDecoder decode();

    /**
     * Helper to serialize and deserialize {@link java.security.KeyStore} to and from byte[].
     */
    SerDe serializeDeserialize();
}
