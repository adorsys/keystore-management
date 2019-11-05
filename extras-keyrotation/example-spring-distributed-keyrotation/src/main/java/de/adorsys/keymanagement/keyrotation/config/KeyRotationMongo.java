package de.adorsys.keymanagement.keyrotation.config;

import com.mongodb.MongoClient;
import de.adorsys.keymanagement.keyrotation.config.properties.JdbcRotationProperties;
import de.adorsys.keymanagement.keyrotation.config.properties.MongoRotationProperties;
import de.adorsys.keymanagement.keyrotation.config.properties.RotationConfiguration;
import de.adorsys.keymanagement.keyrotation.jdbc.JdbcRotationManager;
import de.adorsys.keymanagement.keyrotation.mongo.MongoRotationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Will be enabled if configuration properties have:
 * rotation:
 *   mongo:
 *     enabled: ...
 */
@Configuration
@ConditionalOnProperty(value = "rotation.mongo.enabled", havingValue = "true")
@ConditionalOnBean(MongoRotationProperties.class)
public class KeyRotationMongo {

    /**
     * This bean configures which KeyStore to read/rotate, where to persist the KeyStore and how to lock
     * to prevent concurrent rotations.
     */
    @Bean
    MongoRotationManager mongoRotationManager(MongoRotationProperties properties,
                                        MongoClient client,
                                        RotationConfiguration configuration) {
        return new MongoRotationManager(
                properties.getKeystoreId(),
                client,
                properties.getDatabase(),
                properties.getLockCollection(),
                properties.getKeystoreCollection(),
                configuration.getLockAtMost()
        );
    }
}
