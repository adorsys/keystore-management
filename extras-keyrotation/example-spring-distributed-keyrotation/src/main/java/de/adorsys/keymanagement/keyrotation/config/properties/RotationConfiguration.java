package de.adorsys.keymanagement.keyrotation.config.properties;

import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.api.types.template.GeneratedKeyTemplate;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;
import de.adorsys.keymanagement.keyrotation.api.types.RotationConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

@Data
@Configuration
@ConfigurationProperties(prefix = "rotation")
public class RotationConfiguration implements KeyRotationConfig {

    private Map<KeyType, Integer> countValidByType;
    private Map<KeyType, GeneratedKeyTemplate> keyTemplate = RotationConfig.builder().build().getKeyTemplate();
    private Duration validity;
    private Duration legacy;
    private Duration lockAtMost;
    private KeyStoreSecurityConfig keystore;

    /**
     * Configures KeyStore security.
     */
    private KeyStoreConfig keystoreConfig;

    @Override
    public Supplier<char[]> keyPassword() {
        return () -> keystore.getPassword().toCharArray();
    }

    @Override
    public Supplier<char[]> keyStorePassword() {
        return () -> keystore.getStorePassword().toCharArray();
    }

    @Override
    public Collection<KeyType> getEnabledFor() {
        return countValidByType.keySet();
    }
}
