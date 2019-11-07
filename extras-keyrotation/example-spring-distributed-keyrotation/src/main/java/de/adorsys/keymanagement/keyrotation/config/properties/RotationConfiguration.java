package de.adorsys.keymanagement.keyrotation.config.properties;

import de.adorsys.keymanagement.api.types.template.GeneratedKeyTemplate;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;
import de.adorsys.keymanagement.keyrotation.config.properties.keys.SimpleKeyTemplate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "It is configuration POJO")
public class RotationConfiguration implements KeyRotationConfig {

    /**
     * Count of valid keys in keystore by the type.
     * I.e.
     * rotation:
     *   count-valid-by-type:
     *     SIGNING: 3
     * Will force keystore to have 3 valid signing keys.
     */
    private Map<KeyType, Integer> countValidByType;

    /**
     * Key template for each type. Can be used to change encryption algorithm or key size.
     * I.e.
     * rotation:
     *   key-templates:
     *     secret:
     *       algo: AES
     *       size: 128
     * means that KeyStore will be filled with AES-128 secret keys.
     */
    private SimpleKeyTemplate keyTemplates = new SimpleKeyTemplate();

    /**
     * Duration for key to be valid (valid key can be used for encryption and decryption).
     */
    private Duration validity;

    /**
     * Duration for key to be legacy (legacy key can be used only for decryption).
     */
    private Duration legacy;

    /**
     * Key rotation lock validity interval. Key rotation should be done faster than this value.
     */
    private Duration lockAtMost;

    /**
     * Configures KeyStore security.
     */
    private KeyStoreSecurityConfig keystore;

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

    @Override
    public Map<KeyType, GeneratedKeyTemplate> getKeyTemplate() {
        return keyTemplates.getContainer();
    }
}
