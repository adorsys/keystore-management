package de.adorsys.keymanagement.api.types.template;

import java.util.function.Supplier;

public interface KeyTemplate {

    String generateName();
    Supplier<char[]> getPassword();
}
