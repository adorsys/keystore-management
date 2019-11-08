package de.adorsys.keymanagement.keyrotation;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.test.context.TestExecutionListeners;

/**
 * Ensures that after each test method there is an empty {@code keyrotation} schema;
 * This allows us not to rebuild Application Context on each test method (that is performance boost), but rather to
 * have class-wide DirtiesContext.
 */
@ImportAutoConfiguration(exclude = {
        EmbeddedMongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        MongoAutoConfiguration.class
})
@TestExecutionListeners(value = DatabaseCleaningListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class BaseJdbcDbTest extends BaseNonWebTest {
}
