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

    private String keystoreId;
    private String database;
    private String lockCollection;
    private String keystoreCollection;
}
