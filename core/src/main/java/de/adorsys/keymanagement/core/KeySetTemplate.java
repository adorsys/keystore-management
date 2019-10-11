package de.adorsys.keymanagement.core;

import com.google.common.collect.ImmutableList;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@Builder
public class KeySetTemplate {

    // client may not know key type for provided
    @Singular
    private final ImmutableList<@NonNull ProvidedKey> providedKeys;

    // client may not know key type for provided
    @Singular
    private final ImmutableList<@NonNull ProvidedKeyPair> providedPairs;

    // client may not know key type for provided
    @Singular
    private final ImmutableList<@NonNull ProvidedKeyEntry> providedKeyEntries;

    // client should know what he wants to generate
    @Singular
    private final ImmutableList<@NonNull Secret> generatedSecretKeys;

    @Singular
    private final ImmutableList<@NonNull Encrypting> generatedEncryptionKeys;

    @Singular
    private final ImmutableList<@NonNull Signing> generatedSigningKeys;
}
