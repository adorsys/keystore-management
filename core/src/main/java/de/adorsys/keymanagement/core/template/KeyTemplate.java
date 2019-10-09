package de.adorsys.keymanagement.core.template;

import java.util.function.Supplier;

public interface KeyTemplate {

    String getName();
    Supplier<char[]> getPassword();
}
