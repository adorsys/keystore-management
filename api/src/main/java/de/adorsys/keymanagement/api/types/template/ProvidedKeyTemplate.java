package de.adorsys.keymanagement.api.types.template;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

public interface ProvidedKeyTemplate extends KeyTemplate {

    KeyMetadata getMetadata();
}
