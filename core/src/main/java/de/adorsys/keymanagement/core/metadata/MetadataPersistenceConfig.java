package de.adorsys.keymanagement.core.metadata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MetadataPersistenceConfig {

    @NonNull
    private final Class<? extends KeyMetadata> metadataClass;

    @NonNull
    @Builder.Default
    private final Gson gson = new GsonBuilder().create();
}
