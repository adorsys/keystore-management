package de.adorsys.keymanagement.keyrotation.config;

import de.adorsys.keymanagement.keyrotation.config.properties.JdbcRotationProperties;
import de.adorsys.keymanagement.keyrotation.config.properties.RotationConfiguration;
import de.adorsys.keymanagement.keyrotation.jdbc.JdbcRotationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * JDBC backed rotation and persistence.
 * Will be enabled if configuration properties have:
 * rotation:
 *   jdbc:
 *     enabled: true
 */
@Configuration
@ConditionalOnProperty(value = "rotation.jdbc.enabled", havingValue = "true")
@ConditionalOnBean(JdbcRotationProperties.class)
public class KeyRotationJdbc {

    /**
     * This bean configures which KeyStore to read/rotate, where to persist the KeyStore and how to lock
     * to prevent concurrent rotations.
     */
    @Bean
    JdbcRotationManager jdbcRotationManager(JdbcRotationProperties properties,
                                        DataSource dataSource,
                                        RotationConfiguration configuration) {
        return new JdbcRotationManager(
                properties.getKeystoreId(),
                dataSource,
                properties.getLockTable(),
                properties.getKeystoreTable(),
                configuration.getLockAtMost()
        );
    }
}
