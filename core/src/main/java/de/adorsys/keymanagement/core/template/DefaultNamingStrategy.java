package de.adorsys.keymanagement.core.template;

import java.util.UUID;

public class DefaultNamingStrategy implements KeyNamingStrategy {

    private final String name;
    private final String prefix;

    public DefaultNamingStrategy(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;

        if (null != this.name && null != this.prefix) {
            throw new IllegalArgumentException("Only alias or prefix should be set");
        }
    }

    @Override
    public String generateName() {
        return null != name ? name : generateName(prefix);
    }

    private String generateName(String prefix) {
        return null != prefix ? prefix + UUID.randomUUID().toString() : UUID.randomUUID().toString();
    }
}
