package de.adorsys.keymanagement.api.types.template;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public interface ProvidedKeyTemplate extends KeyTemplate {

    /**
     * Allows to get all metadata info stored within key entry
     * @return metadata saved inside key entry
     */
    KeyMetadata getMetadata();
}
