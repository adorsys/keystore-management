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

import java.security.spec.AlgorithmParameterSpec;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class Signing implements GeneratedKeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate(excludes = KeyPairEncryptionTemplate.ExcludeToBuilder.class)
    private final KeyPairEncryptionTemplate encryptionTemplate;

    private final KeyMetadata metadata;

    public Collection<Signing> repeat(int times) {
        return IntStream.range(0, times).boxed().map(it -> this.toBuilder().build()).collect(Collectors.toList());
    }

    @Builder(builderClassName = "Templated", toBuilder = true)
    Signing(@NonNull KeyTemplate keyTemplate, @NonNull KeyPairEncryptionTemplate encryptionTemplate,
            KeyMetadata metadata) {
        this.keyTemplate = keyTemplate;
        this.metadata = metadata;
        this.encryptionTemplate = encryptionTemplate;
    }

    @SuppressWarnings("checkstyle:ParameterNumber") // Is a builder method
    @Builder(builderMethodName = "with")
    Signing(String alias, String prefix, Supplier<char[]> password, String algo, String sigAlgo,
            Integer keySize, KeyMetadata metadata, String commonName, AlgorithmParameterSpec paramSpec) {
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password);
        this.metadata = metadata;
        this.encryptionTemplate = KeyPairEncryptionTemplate.of(algo, keySize, sigAlgo, commonName, paramSpec);
    }
}
