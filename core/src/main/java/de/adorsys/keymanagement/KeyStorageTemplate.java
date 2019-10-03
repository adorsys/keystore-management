package de.adorsys.keymanagement;

import com.google.common.collect.ImmutableList;
import de.adorsys.keymanagement.template.generated.Encrypting;
import de.adorsys.keymanagement.template.generated.Secret;
import de.adorsys.keymanagement.template.generated.Signing;
import de.adorsys.keymanagement.template.provided.KeyEntry;
import de.adorsys.keymanagement.template.provided.Provided;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.function.Supplier;

@Getter
@Accessors(fluent = true)
@SuperBuilder
public class KeyStorageTemplate {

    // Maybe set on key level
    Supplier<char[]> keyPassword;

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
