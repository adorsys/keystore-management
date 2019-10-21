package de.adorsys.keymanagement.api.persist;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public interface KeyMetadataPersister {

    void persistMetadata(String forAlias, KeyMetadata metadata);
    void removeMetadata(String forAlias);
}
