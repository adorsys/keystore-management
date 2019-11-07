package de.adorsys.keymanagement.api.types.template.generated;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.template.DefaultNamingStrategy;
import de.adorsys.keymanagement.api.types.template.GeneratedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.KeyTemplate;
import de.adorsys.keymanagement.api.types.template.NameAndPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Password-based key entry, can be used to store some string/byte-based secret that is not key in keystore too.
 */
@Getter
public class Pbe implements GeneratedKeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate(excludes = PbeKeyEncryptionTemplate.ExcludeToBuilder.class)
    private final PbeKeyEncryptionTemplate encryptionTemplate;

    private final char[] data;
    private final KeyMetadata metadata;

    public Collection<Pbe> repeat(int times) {
        return IntStream.range(0, times).boxed().map(it -> this.toBuilder().build()).collect(Collectors.toList());
    }

    @Builder(builderClassName = "Templated", toBuilder = true)
    Pbe(@NonNull char[] data, @NonNull KeyTemplate keyTemplate, @NonNull PbeKeyEncryptionTemplate encryptionTemplate,
        KeyMetadata metadata) {
        this.data = data;
        this.keyTemplate = keyTemplate;
        this.metadata = metadata;
        this.encryptionTemplate = encryptionTemplate;
    }

    @SuppressWarnings("checkstyle:ParameterNumber") // Is a builder method
    @Builder(builderMethodName = "with")
    Pbe(@NonNull char[] data, String alias, String prefix, Supplier<char[]> password, String algo, Integer saltLen,
        Integer iterCount, KeyMetadata metadata) {
        this.data = data;
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password);
        this.metadata = metadata;
        this.encryptionTemplate = PbeKeyEncryptionTemplate.of(algo, saltLen, iterCount);
    }
}
