package de.adorsys.keymanagement.core.template.provided;

import de.adorsys.keymanagement.core.template.KeyTemplate;
import de.adorsys.keymanagement.core.template.NameAndPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.security.KeyStore;
import java.util.function.Supplier;

@Getter
public class KeyEntry implements KeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate
    private final KeyStore.Entry entry;

    @Builder(builderClassName = "Templated", toBuilder = true)
    KeyEntry(@NonNull KeyTemplate keyTemplate, @NonNull KeyStore.Entry entry) {
        this.keyTemplate = keyTemplate;
        this.entry = entry;
    }

    @Builder(builderMethodName = "with")
    KeyEntry(String alias, String prefix, Supplier<char[]> password, @NonNull KeyStore.Entry entry) {
        this.keyTemplate = new NameAndPassword(alias, prefix, password);
        this.entry = entry;
    }
}
