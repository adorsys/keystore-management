package de.adorsys.keymanagement.core.template;

public interface KeyTemplate {
    String getAlias();

    String getPrefix();

    java.util.function.Supplier<char[]> getPassword();
}
