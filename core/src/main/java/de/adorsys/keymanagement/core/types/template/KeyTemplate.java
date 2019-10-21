package de.adorsys.keymanagement.core.types.template;

import java.util.function.Supplier;

public interface KeyTemplate {

    String generateName();
    Supplier<char[]> getPassword();
}
