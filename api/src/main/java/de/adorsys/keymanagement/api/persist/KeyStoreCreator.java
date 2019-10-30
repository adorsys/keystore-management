package de.adorsys.keymanagement.api.persist;

import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.api.types.source.KeySet;

import java.security.KeyStore;
import java.util.function.Supplier;

public interface KeyStoreCreator {

    /**
     * Configures keystore before generation with custom parameters. Without it keystore with default values
     * specified in {@link KeyStoreConfig} will be created
     * @param config
     * @return configured {@link de.adorsys.keymanagement.api.persist.KeyStoreCreator}
     */
    KeyStoreCreator withConfig(KeyStoreConfig config);

    /**
     * Generates {@link java.security.KeyStore} from key set. Additionally persists key metadata if
     * {@link de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence} is configured.
     * @param keySet Set of keys to be stored in  KeyStore.
     * @return Resulting keystore with key metadata (metadata if required).
     */
    KeyStore generate(KeySet keySet);

    /**
     * Generates {@link java.security.KeyStore} from key set. Additionally persists key metadata if
     * {@link de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence} is configured.
     * @param keySet Set of keys to be stored in  KeyStore.
     * @param defaultKeyPassword Password to protect key in KeyStore if its individual password is missing.
     * @return Resulting keystore with key metadata (metadata if required).
     */
    KeyStore generate(KeySet keySet, Supplier<char[]> defaultKeyPassword);

    /**
     * Generates {@link java.security.KeyStore} from key set. Key metadata is not persisted and generated.
     * @param keySet Set of keys to be stored in  KeyStore.
     * @return Resulting keystore without metadata.
     */
    KeyStore generateWithoutMetadata(KeySet keySet);

    /**
     * Generates {@link java.security.KeyStore} from key set. Key metadata is not persisted and generated.
     * @param keySet Set of keys to be stored in  KeyStore.
     * @param defaultKeyPassword Password to protect key in KeyStore if its individual password is missing.
     * @return Resulting keystore without metadata.
     */
    KeyStore generateWithoutMetadata(KeySet keySet, Supplier<char[]> defaultKeyPassword);
}
