package de.adorsys.keymanagement.core.template;

import java.util.function.Supplier;

public interface KeyTemplate {

    String getAlias();
    String getPrefix();
    Supplier<char[]> getPassword();
}
