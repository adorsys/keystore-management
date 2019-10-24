package de.adorsys.keymanagement.api.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

import java.security.KeyStore;

public class NoOpMetadataPersistence implements KeyMetadataPersistence {

    @Override
    public boolean isMetadataEntry(String forAlias, KeyStore keyStore) {
        return false;
    }

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
