package de.adorsys.keymanagement.core.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

import java.security.KeyStore;

/**
 * It is just an indicator class, true implementation is provided by {@link ToKeyStoreMetadataPersister}.
 */
public final class WithPersister implements MetadataPersister {

    @Override
    public KeyMetadata extract(String forAlias, KeyStore keyStore) {
        return null;
    }

    @Override
    public void persistMetadata(String forAlias, KeyMetadata metadata, KeyStore keyStore) {
        // NOP
    }

    @Override
    public void removeMetadata(String forAlias, KeyStore keyStore) {
        // NOP
    }
}
