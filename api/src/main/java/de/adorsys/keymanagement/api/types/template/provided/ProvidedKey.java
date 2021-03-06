package de.adorsys.keymanagement.api.types.template.provided;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.template.DefaultNamingStrategy;
import de.adorsys.keymanagement.api.types.template.KeyTemplate;
import de.adorsys.keymanagement.api.types.template.NameAndPassword;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.security.Key;
import java.util.function.Supplier;

@Getter
public class ProvidedKey implements ProvidedKeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate
    private final Key key;

    @Getter
    private final KeyMetadata metadata;

    @Builder(builderClassName = "Templated", toBuilder = true)
    ProvidedKey(@NonNull KeyTemplate keyTemplate, @NonNull Key key, KeyMetadata metadata) {
        this.keyTemplate = keyTemplate;
        this.metadata = metadata;
        this.key = key;
    }

    @SuppressWarnings("checkstyle:ParameterNumber") // Is a builder method
    @Builder(builderMethodName = "with")
    ProvidedKey(String alias, String prefix, Supplier<char[]> password, @NonNull Key key, KeyMetadata metadata) {
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password);
        this.metadata = metadata;
        this.key = key;
    }
}
