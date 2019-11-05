package de.adorsys.keymanagement.keyrotation.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty("rotation.mongo.keystore-id")
@ConfigurationProperties(prefix = "rotation.mongo")
public class MongoRotationProperties {

    /**
     * KeyStore ID that will be rotated.
     */
    private String keystoreId;

    /**
     * Mongo database to use.
     */
    private String database;

    /**
     * Collection that contains key rotation lock to prevent concurrent key rotation.
     */
    private String lockCollection;

    /**
     * Collection with KeyStore blobs that are identified by {@code keystoreId}.
     */
    private String keystoreCollection;
}
