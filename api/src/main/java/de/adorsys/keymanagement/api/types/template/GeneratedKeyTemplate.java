package de.adorsys.keymanagement.api.types.template;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public interface GeneratedKeyTemplate extends KeyTemplate {

    /**
     * Gets metadata associated with KeyTemplate
     * @return key metadata
     */
    KeyMetadata getMetadata();
}
