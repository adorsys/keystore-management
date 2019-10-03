package de.adorsys.keymanagement.template;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.function.Supplier;

@Getter
@SuperBuilder(toBuilder = true)
public class NamedWithPassword {

    private final String alias;
    private final String prefix;
    private final Supplier<char[]> password;
}
