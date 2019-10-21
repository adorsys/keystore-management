package de.adorsys.keymanagement.api.source;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public interface KeyMetadataExtractor {

    KeyMetadata extract(String forAlias);
}
