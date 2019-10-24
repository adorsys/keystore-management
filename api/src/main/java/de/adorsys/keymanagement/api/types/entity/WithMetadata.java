package de.adorsys.keymanagement.api.types.entity;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class WithMetadata<T> {

    @NonNull
    private final T key;

    private final KeyMetadata metadata;

    /**
     * Indicates whether given alias points not to a true key, but to key metadata.
     */
    private final boolean metadataEntry;
}
