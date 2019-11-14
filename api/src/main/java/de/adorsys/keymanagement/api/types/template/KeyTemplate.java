package de.adorsys.keymanagement.api.types.template;

import java.util.function.Supplier;

public interface KeyTemplate {

    /**
     * Generates alias for new key
     * @return generated or hardcoded key name
     */
    String generateName();

    /**
     * password used to access key in keystore
     * @return password as a char array supplier
     */
    Supplier<char[]> getPassword();
}
