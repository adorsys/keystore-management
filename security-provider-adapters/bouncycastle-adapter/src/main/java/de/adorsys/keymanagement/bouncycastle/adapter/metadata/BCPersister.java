package de.adorsys.keymanagement.bouncycastle.adapter.metadata;

import de.adorsys.keymanagement.api.metadata.KeyMetadataPersistence;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

// It is just indicator class, true implementation is provided by DefaultBCMetadataPersister
public final class BCPersister implements KeyMetadataPersistence {

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
