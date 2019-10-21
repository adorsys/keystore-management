package de.adorsys.keymanagement.api.types.template.provided;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.DefaultNamingStrategy;
import de.adorsys.keymanagement.api.types.template.KeyTemplate;
import de.adorsys.keymanagement.api.types.template.NameAndPassword;
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

    @Builder(builderClassName = "Templated", toBuilder = true)
    ProvidedKey(@NonNull KeyTemplate keyTemplate, @NonNull Key key) {
        this.keyTemplate = keyTemplate;
        this.key = key;
    }

    @Builder(builderMethodName = "with")
    ProvidedKey(String alias, String prefix, Supplier<char[]> password, @NonNull Key key, KeyMetadata metadata) {
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password, metadata);
        this.key = key;
    }
}
