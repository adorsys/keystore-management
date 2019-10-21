package de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore;

/**
 * Wrapper for password for reading secret or private key entry.
 */
public class ReadKeyPassword extends BaseTypePasswordString {

    public ReadKeyPassword(String readKeyPassword) {
        super(readKeyPassword);
    }

    public ReadKeyPassword(char[] readKeyPassword) {
        super(readKeyPassword);
    }
}
