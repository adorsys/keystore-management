package de.adorsys.keymanagement.core.template;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class NameAndPassword implements KeyTemplate {

    private final String alias;
    private final String prefix;
    private final Supplier<char[]> password;
}
