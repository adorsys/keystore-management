package de.adorsys.keymanagement.keyrotation;

import com.mongodb.MongoClient;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.util.function.Consumer;

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
public abstract class BaseMongoDbTest extends BaseNonWebTest {

    @Autowired
    private MongoClient client;

    @Autowired
    private RotationLocker locker;

    @AfterEach
    @SneakyThrows
    void cleanUp() {
        client.listDatabaseNames().forEach((Consumer<? super String>) it -> client.getDatabase(it).drop());
        locker.clearCache();
    }
}
