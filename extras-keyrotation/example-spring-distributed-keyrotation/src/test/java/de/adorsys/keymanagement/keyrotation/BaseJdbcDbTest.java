package de.adorsys.keymanagement.keyrotation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * Ensures that after each test method there is an empty {@code sts} schema;
 */
public abstract class BaseJdbcDbTest extends BaseSpringTest {

    @Autowired
    private Environment env;

    @Autowired
    private JdbcOperations jdbcOper;

    @Autowired
    private DataSourceProperties properties;


    @BeforeEach
    void zzz() {
        properties.getData();
    }

    @AfterEach
    void destroyAndCreateEmptySchema() {
        if (env.acceptsProfiles(Profiles.of("postgres"))) {
            jdbcOper.update("DROP SCHEMA IF EXISTS sts CASCADE");
        } else {
            jdbcOper.update("DROP SCHEMA IF EXISTS sts");
        }

        jdbcOper.update("CREATE SCHEMA sts");
    }
}
