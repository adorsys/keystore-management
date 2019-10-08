package de.adorsys.keymanagement.core.template.generated;

import de.adorsys.keymanagement.core.template.KeyTemplate;
import de.adorsys.keymanagement.core.template.NameAndPassword;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Signing implements KeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate(excludes = KeyPairEncryptionTemplate.ExcludeToBuilder.class)
    private final KeyPairEncryptionTemplate encryptionTemplate;

    public Collection<Signing> repeat(int times) {
        return IntStream.range(0, times).boxed().map(it -> this.toBuilder().build()).collect(Collectors.toList());
    }

    @Builder(builderClassName = "Templated", toBuilder = true)
    Signing(@NonNull KeyTemplate keyTemplate, @NonNull KeyPairEncryptionTemplate encryptionTemplate) {
        this.keyTemplate = keyTemplate;
        this.encryptionTemplate = encryptionTemplate;
    }

    @Builder(builderMethodName = "with")
    Signing(String alias, String prefix, Supplier<char[]> password, String algo, String sigAlgo,
            Integer keySize) {
        this.keyTemplate = new NameAndPassword(alias, prefix, password);
        this.encryptionTemplate = KeyPairEncryptionTemplate.of(algo, keySize, sigAlgo);
    }
}
