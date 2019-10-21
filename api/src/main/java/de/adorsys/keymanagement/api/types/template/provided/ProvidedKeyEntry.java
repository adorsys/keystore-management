package de.adorsys.keymanagement.api.types.template.provided;

import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.DefaultNamingStrategy;
import de.adorsys.keymanagement.api.types.template.KeyTemplate;
import de.adorsys.keymanagement.api.types.template.NameAndPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.security.KeyStore;
import java.util.function.Supplier;

@Getter
public class ProvidedKeyEntry implements ProvidedKeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate
    private final KeyStore.Entry entry;

    @Builder(builderClassName = "Templated", toBuilder = true)
    ProvidedKeyEntry(@NonNull KeyTemplate keyTemplate, @NonNull KeyStore.Entry entry) {
        this.keyTemplate = keyTemplate;
        this.entry = entry;
    }

    @Builder(builderMethodName = "with")
    ProvidedKeyEntry(String alias, String prefix, Supplier<char[]> password, @NonNull KeyStore.Entry entry) {
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password);
        this.entry = entry;
    }
}
