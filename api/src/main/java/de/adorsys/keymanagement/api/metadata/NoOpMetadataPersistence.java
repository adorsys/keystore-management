package de.adorsys.keymanagement.api.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public class NoOpMetadataPersistence implements KeyMetadataPersistence {

    @Override
    public KeyMetadata extract(String forAlias) {
        return null;
    }

    @Override
    public void persistMetadata(String forAlias, KeyMetadata metadata) {
        // NOP
    }

    @Override
    public void removeMetadata(String forAlias) {
        // NOP
    }
}
