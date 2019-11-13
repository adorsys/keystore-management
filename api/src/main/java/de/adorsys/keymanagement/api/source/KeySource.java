package de.adorsys.keymanagement.api.source;

import de.adorsys.keymanagement.api.types.entity.AliasWithMeta;
import de.adorsys.keymanagement.api.types.entity.WithMetadata;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.Set;
import java.util.stream.Stream;

public interface KeySource {

    /**
     * Reads all key aliases from key source with corresponding key metadata
     * @return stream of key aliases with metadata
     */
    Stream<WithMetadata<String>> aliases();

    /**
     * Reads key aliases with metadata filtered by key type
     * @param clazz type of keys to filter
     * @param <T> acts as a type-selector so can be safely used for KeyStore too
     * @return aliases from keysource with given type of keys
     */
    <T extends ProvidedKeyTemplate> Stream<WithMetadata<String>> aliasesFor(Class<T> clazz);

    /**
     * Reads metadata by alias from keysource
     * @param alias name used to find key
     * @return alias with metadata
     */
    WithMetadata<String> asAliasWithMeta(String alias);

    /**
     * Finds key by alias and represents it as key entry with metadata
     * @param alias name used to find entry
     * @return selected by alias keystore entry
     */
    WithMetadata<KeyStore.Entry> asEntry(String alias);

    /**
     * Finds key by alias and represents it as key pair with metadata
     * @param alias name used to find key pair
     * @return selected by alias key pair
     */
    WithMetadata<KeyPair> asPair(String alias);

    /**
     * Finds key by alias and returns it with metadata
     * @param alias ame used to find key
     * @return selected by alias key
     */
    WithMetadata<Key> asKey(String alias);

    /**
     * Removes key by keyId from key source
     * @param keyId key alias
     */
    void remove(String keyId);

    /**
     * Adds key to key source
     * @param keyTemplate keeps key, key metadata and generates new alias for the key
     * @return generated key alias
     */
    String addAndReturnId(ProvidedKeyTemplate keyTemplate);

    /**
     * Updates metadata for specified by alias key
     * @param aliasWithMetadata new metadata and key alias
     */
    void updateMetadata(AliasWithMeta aliasWithMetadata);

    /**
     * List all child entries ids that are associated with given key id (i.e. metadata), INCLUDING itself
     */
    Set<String> allAssociatedEntries(String keyId);
}
