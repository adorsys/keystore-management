package de.adorsys.keymanagement.deprecated.types.keystore;

/**
 * Wrapper for keystore serialization/deserialization password as well as password for reading public keys.
 */
public class ReadStorePassword extends BaseTypePasswordString {

    public ReadStorePassword(String readStorePassword) {
        super(readStorePassword);
    }
}
