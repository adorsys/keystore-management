package de.adorsys.keymanagement.generator;

import de.adorsys.keymanagement.generator.types.PasswordBasedKeyConfig;

public class DefaultPasswordBasedKeyConfig implements PasswordBasedKeyConfig {

    public DefaultPasswordBasedKeyConfig() {
    }

    @Override
    public String secretKeyFactoryId() {
        return "PBEWithHmacSHA256AndAES_256";
    }
}
