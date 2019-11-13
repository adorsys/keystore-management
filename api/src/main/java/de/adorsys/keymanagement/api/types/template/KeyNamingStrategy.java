package de.adorsys.keymanagement.api.types.template;

/**
 * Interface which determines algorithm for generating key name
 */
public interface KeyNamingStrategy {

    /**
     * @return generated key name
     */
    String generateName();
}
