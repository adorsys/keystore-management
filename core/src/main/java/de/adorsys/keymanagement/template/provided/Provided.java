package de.adorsys.keymanagement.template.provided;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.security.Key;

@Getter
@SuperBuilder(builderMethodName = "with")
public class Provided {

    private final String alias;
    private final String prefix;

    @NonNull
    private final Key key;
}
