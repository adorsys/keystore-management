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

@Getter
public class Secret implements GeneratedKeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate(excludes = SecretKeyEncryptionTemplate.ExcludeToBuilder.class)
    private final SecretKeyEncryptionTemplate encryptionTemplate;

    private final KeyMetadata metadata;

    public Collection<Secret> repeat(int times) {
        return IntStream.range(0, times).boxed().map(it -> this.toBuilder().build()).collect(Collectors.toList());
    }

    @Builder(builderClassName = "Templated", toBuilder = true)
    Secret(@NonNull KeyTemplate keyTemplate, @NonNull SecretKeyEncryptionTemplate encryptionTemplate,
           KeyMetadata metadata) {
        this.keyTemplate = keyTemplate;
        this.metadata = metadata;
        this.encryptionTemplate = encryptionTemplate;
    }

    @SuppressWarnings("checkstyle:ParameterNumber") // Is a builder method
    @Builder(builderMethodName = "with")
    Secret(String alias, String prefix, Supplier<char[]> password, String algo, Integer keySize,
                  KeyMetadata metadata) {
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password);
        this.metadata = metadata;
        this.encryptionTemplate = SecretKeyEncryptionTemplate.of(algo, keySize);
    }
}
