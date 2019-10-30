package de.adorsys.keymanagement.api.persist;

import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface SerDe {

    /**
     * Configures keystore before deserialization with custom parameters. Without it keystore with default values
     * specified in {@link KeyStoreConfig} will be created
     * @param config
     * @return configured {@link de.adorsys.keymanagement.api.persist.SerDe}
     */
    SerDe withConfig(KeyStoreConfig config);

    /**
     * Serializes {@link java.security.KeyStore} into byte[].
     * @param keyStore KeyStore to serialize
     * @param keyStorePassword KeyStore read protection password.
     * @return KeyStore byte[] representation.
     */
    byte[] serialize(KeyStore keyStore, Supplier<char[]> keyStorePassword);

    /**
     * Deserializes {@link java.security.KeyStore} from byte[].
     * @param keyStore KeyStore byte[] to deserialize
     * @param keyStorePassword KeyStore read protection password.
     * @return KeyStore instance.
     */
    KeyStore deserialize(byte[] keyStore, Supplier<char[]> keyStorePassword);
}
