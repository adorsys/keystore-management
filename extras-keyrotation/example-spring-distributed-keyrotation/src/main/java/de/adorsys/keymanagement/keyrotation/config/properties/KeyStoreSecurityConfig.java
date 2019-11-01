package de.adorsys.keymanagement.keyrotation.config.properties;

import lombok.Data;

@Data
public class KeyStoreSecurityConfig {

    private String password;
    private String storePassword;
    private String type;
}
