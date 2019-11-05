package de.adorsys.keymanagement.keyrotation.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty("rotation.jdbc.keystore-id")
@ConfigurationProperties(prefix = "rotation.jdbc")
public class JdbcRotationProperties {

    /**
     * KeyStore ID that will be rotated.
     */
    private String keystoreId;

    /**
     * Table that contains key rotation lock to prevent concurrent key rotation.
     */
    private String lockTable;

    /**
     * Table with KeyStore blobs that are identified by {@code keystoreId}.
     */
    private String keystoreTable;
}
