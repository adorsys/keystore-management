package de.adorsys.keymanagement.core.template.provided;

import de.adorsys.keymanagement.core.template.KeyTemplate;
import de.adorsys.keymanagement.core.template.NameAndPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Delegate;

import java.security.Key;
import java.util.function.Supplier;

@Getter
public class Provided implements KeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate
    private final Key key;

    @Builder(builderClassName = "Templated", toBuilder = true)
    Provided(@NonNull KeyTemplate keyTemplate, @NonNull Key key) {
        this.keyTemplate = keyTemplate;
        this.key = key;
    }

    @Builder(builderMethodName = "with")
    Provided(String alias, String prefix, Supplier<char[]> password, @NonNull Key key) {
        this.keyTemplate = new NameAndPassword(alias, prefix, password);
        this.key = key;
    }
}
