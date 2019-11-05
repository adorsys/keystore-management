package de.adorsys.keymanagement.keyrotation;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.jdbc.core.JdbcOperations;

/**
 * Ensures that after each test method there is an empty {@code keyrotation} schema;
 */
@ImportAutoConfiguration(exclude = {
        EmbeddedMongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        MongoAutoConfiguration.class
})
public abstract class BaseJdbcDbTest extends BaseSpringTest {

    @Autowired
    private Environment env;

    @Autowired
    private JdbcOperations jdbcOper;

    @AfterEach
    void destroyAndCreateEmptySchema() {
        if (env.acceptsProfiles(Profiles.of("postgres"))) {
            jdbcOper.update("DROP SCHEMA IF EXISTS keyrotation CASCADE");
        } else {
            jdbcOper.update("DROP SCHEMA IF EXISTS keyrotation");
        }

        jdbcOper.update("CREATE SCHEMA keyrotation");
    }
}
