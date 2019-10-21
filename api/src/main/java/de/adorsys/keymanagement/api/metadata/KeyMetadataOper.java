package de.adorsys.keymanagement.api.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public interface KeyMetadataOper {

    KeyMetadata extract(String forAlias);
    void persistMetadata(String forAlias, KeyMetadata metadata);
    void removeMetadata(String forAlias);
}
