package de.adorsys.keymanagement.generator.deprecated.types.keystore;


/**
 * Generates random secret key entry.
 */
public interface SecretKeyGenerator {

    /**
     * Create random secret key.
     * @param alias Secret key alias.
     * @param readKeyPassword Password to read this key
     * @return Generated secret key
     */
    SecretKeyEntry generate(String alias, ReadKeyPassword readKeyPassword);
}
