package de.adorsys.keymanagement.keyrotation;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Ensures that after each test method there is an empty {@code sts} schema;
 */
@Import({MongoDataAutoConfiguration.class, MongoAutoConfiguration.class})
@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
public abstract class BaseMongoDbTest extends BaseSpringTest {
}
