package de.adorsys.keymanagement.api.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.security.KeyStore;

public interface KeyMetadataOper {

    /**
     * Password that will be used to store metadata as key entry in encrypted KeySource (i.e. java Keystore).
     * By default password is null for keys of metadata type
     * @param forAlias name used to find key
     * @return password for specified alias as byte array
     */
    @Nullable
    @SuppressFBWarnings("PZLA_PREFER_ZERO_LENGTH_ARRAYS")
    default char[] metadataPassword(String forAlias) {
        return null;
    }

    /**
     * Generates metadata key entry alias from key alias
     * @param forAlias key name used to find key in keystore
     * @return metadata alias
     */
    String metadataAliasForKeyAlias(String forAlias);

    /**
     * Checks if key for provided entry alias is metadata-entry
     * @param forAlias name used to find key
     * @param keyStore Keystore where search entry
     * @return true if entry contains metadata
     */
    boolean isMetadataEntry(String forAlias, KeyStore keyStore);

    /**
     * Reads metadata entry for key (by its alias) from KeyStore
     * @param forAlias name used to find key
     * @param keyStore Keystore where search key
     * @return key metadata
     */
    KeyMetadata extract(String forAlias, KeyStore keyStore);

    /**
     * Saves metadata for existing key in keystore
     * @param forAlias name used to find key
     * @param metadata new key metadata
     * @param keyStore which consists key to which metadata will be added
     */
    void persistMetadata(String forAlias, KeyMetadata metadata, KeyStore keyStore);

    /**
     * Removes key metadata from KeyStore
     * @param forAlias name used to find key
     * @param keyStore key store to remove metadata from
     */
    void removeMetadata(String forAlias, KeyStore keyStore);
}
