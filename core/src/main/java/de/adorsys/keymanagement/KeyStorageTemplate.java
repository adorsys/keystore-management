package de.adorsys.keymanagement;

import com.google.common.collect.ImmutableList;
import de.adorsys.keymanagement.template.KeyTemplate;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

import java.security.Key;

@Builder
public class KeyStorageTemplate {

    @Singular
    private final ImmutableList<@NonNull Key> withKeys;

    @Singular
    private final ImmutableList<@NonNull KeyTemplate> withGeneratedKeys;
}
