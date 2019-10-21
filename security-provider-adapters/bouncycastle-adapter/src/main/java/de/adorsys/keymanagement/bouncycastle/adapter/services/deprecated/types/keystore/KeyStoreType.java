package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.keystore;

import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.BaseTypeString;

/**
 * Wrapper for keystore type - example: PKCS12,JKS,UBER.
 */
public class KeyStoreType extends BaseTypeString {

    public static KeyStoreType DEFAULT = getDefaultKeyStoreType();

    public KeyStoreType(String value) {
        super(value);
    }

    protected static KeyStoreType getDefaultKeyStoreType() {
        String serverKeystoreType = System.getProperty("SERVER_KEYSTORE_TYPE");
        if (null != serverKeystoreType && !serverKeystoreType.isEmpty()) {
            return new KeyStoreType(serverKeystoreType);
        }
        return new KeyStoreType("UBER");
    }
}
