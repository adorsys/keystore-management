package de.adorsys.keymanagement.api.types.entity;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(toBuilder = true)
public class AliasWithMeta<T extends KeyMetadata> {

    @NonNull
    private final String alias;

    private final T metadata;
}
