package de.adorsys.keymanagement.keyrotation.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rotation.jdbc")
public class JdbcRotationProperties {

    private String keystoreId;
    private String lockTable;
    private String keystoreTable;
}
