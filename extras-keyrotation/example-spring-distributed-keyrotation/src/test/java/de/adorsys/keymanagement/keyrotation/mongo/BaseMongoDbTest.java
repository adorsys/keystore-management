package de.adorsys.keymanagement.keyrotation.mongo;

import de.adorsys.keymanagement.keyrotation.BaseNonWebTest;
import de.adorsys.keymanagement.keyrotation.PopRotationValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

/**
 * Ensures that after each test method there is an empty {@code keyrotation} database;
 * This allows us not to rebuild Application Context on each test method (that is performance boost), but rather to
 * have class-wide DirtiesContext.
 */
@Import({MongoDataAutoConfiguration.class, MongoAutoConfiguration.class})
@ImportAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        FlywayAutoConfiguration.class
})
@ActiveProfiles(profiles = "mongo")
@Testcontainers
public class BaseMongoDbTest extends BaseNonWebTest {

    public MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");

    @Autowired
    private PopRotationValidator validator;

    @Test
    void testPopRotates() {
        mongoDBContainer.setPortBindings(List.of("27017:27017"));
        mongoDBContainer.start();
        validator.testPopRotates();
    }
}
