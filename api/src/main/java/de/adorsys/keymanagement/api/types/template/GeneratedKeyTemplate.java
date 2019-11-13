package de.adorsys.keymanagement.api.types.template;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public interface GeneratedKeyTemplate extends KeyTemplate {

    /**
     * Access to metadata stored in key template
     * @return key metadata
     */
    KeyMetadata getMetadata();
}
