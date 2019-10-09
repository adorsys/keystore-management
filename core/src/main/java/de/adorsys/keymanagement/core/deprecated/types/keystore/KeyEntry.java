package de.adorsys.keymanagement.core.deprecated.types.keystore;

/**
 * Wrapper for key entry within keystore.
 */
public interface KeyEntry {

    /**
     * Password to read key from keystore
     */
    ReadKeyPassword getReadKeyPassword();

    /**
     * Key alias
     */
    String getAlias();
}
