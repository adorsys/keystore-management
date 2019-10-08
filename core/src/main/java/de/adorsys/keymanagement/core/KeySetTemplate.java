package de.adorsys.keymanagement.core;

import com.google.common.collect.ImmutableList;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.KeyEntry;
import de.adorsys.keymanagement.core.template.provided.Provided;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

@Getter
@Accessors(fluent = true)
@Builder
public class KeySetTemplate {

    // Maybe set on key level
    private final Supplier<char[]> keyPassword;

    // client may not know key type for provided
    @Singular
    private final ImmutableList<@NonNull Provided> providedKeys;

    // client may not know key type for provided
    @Singular
    private final ImmutableList<@NonNull KeyEntry> providedKeyEntries;

    // client should know what he wants to generate
    @Singular
    private final ImmutableList<@NonNull Secret> generatedSecretKeys;

    @Singular
    private final ImmutableList<@NonNull Encrypting> generatedEncryptionKeys;

    @Singular
    private final ImmutableList<@NonNull Signing> generatedSigningKeys;
}
