package de.adorsys.keymanagement.keyrotation;

import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.jdbc.DatabaseType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

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
@TestExecutionListeners(value = BaseJdbcDbTest.ExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class BaseJdbcDbTest extends BaseSpringTest {

    // This value ensures that Flyway migration will run before @Sql listener and @Transactional listener
    static final int ORDER = 3000;

    public static class ExecutionListener extends AbstractTestExecutionListener {

        private static final Map<DatabaseType, String> DROP_STATEMENTS_BY_DB_TYPE = ImmutableMap.of(
                DatabaseType.POSTGRESQL, "DROP SCHEMA IF EXISTS keyrotation CASCADE",
                DatabaseType.H2, "DROP SCHEMA IF EXISTS keyrotation CASCADE",
                DatabaseType.MYSQL, "DROP SCHEMA IF EXISTS keyrotation"
        );

        @Override
        public int getOrder() {
            return ORDER;
        }

        /**
         * This assumes that database has empty schema, flyway will migrate it.
         */
        @Override
        public void beforeTestMethod(TestContext testContext) {
            ApplicationContext appContext = testContext.getApplicationContext();
            Flyway flyWay = appContext.getBean(Flyway.class);
            flyWay.migrate();
        }

        /**
         * This drops database schema after test is done, so that next test method will work on clean DB,
         * this way we spend less time because we do not need to start DB and clean Spring context each test
         * method.
         * Also this cleans up ShedLock cached locks as they also contribute to state.
         */
        @Override
        public void afterTestMethod(TestContext testContext) {
            ApplicationContext appContext = testContext.getApplicationContext();
            JdbcOperations oper = appContext.getBean(JdbcOperations.class);
            RotationLocker locker = appContext.getBean(RotationLocker.class);
            locker.clearCache();
            destroyAndCreateEmptySchema(oper);
        }

        void destroyAndCreateEmptySchema(JdbcOperations oper) {
            DatabaseType type = oper.execute(DatabaseType::fromJdbcConnection);
            String dropStatement = DROP_STATEMENTS_BY_DB_TYPE.getOrDefault(
                    type,
                    DROP_STATEMENTS_BY_DB_TYPE.get(DatabaseType.MYSQL)
            );
            oper.execute(dropStatement);

            oper.update("CREATE SCHEMA keyrotation");
        }
    }
}
