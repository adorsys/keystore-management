package de.adorsys.keymanagement.keyrotation.config.properties;

import lombok.Data;

@Data
public class KeyStoreSecurityConfig {

    /**
     * Password to read key from KeyStore.
     */
    private String password;

    /**
     * Password to open KeyStore.
     */
    private String storePassword;

    /**
     * KeyStore type to use (i.e. BCFKS allows FIPS level of KeyStore protection).
     */
    private String type;
}
