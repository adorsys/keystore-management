package de.adorsys.keymanagement.template.provided;

import de.adorsys.keymanagement.template.NamedWithPassword;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.security.KeyStore;

@Getter
@SuperBuilder(builderMethodName = "with", toBuilder = true)
public class KeyEntry extends NamedWithPassword {

    @NonNull
    private final KeyStore.Entry entry;
}
