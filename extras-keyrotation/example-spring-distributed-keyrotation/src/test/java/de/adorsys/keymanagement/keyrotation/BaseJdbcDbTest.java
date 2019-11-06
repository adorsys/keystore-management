package de.adorsys.keymanagement.keyrotation;

import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Ensures that after each test method there is an empty {@code keyrotation} schema;
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

        @Override
        public int getOrder() {
            return ORDER;
        }

        @Override
        public void beforeTestMethod(TestContext testContext) {
            ApplicationContext appContext = testContext.getApplicationContext();
            Flyway flyWay = appContext.getBean(Flyway.class);
            flyWay.migrate();
        }

        @Override
        public void afterTestMethod(TestContext testContext) {
            ApplicationContext appContext = testContext.getApplicationContext();
            JdbcOperations oper = appContext.getBean(JdbcOperations.class);
            Environment env = appContext.getBean(Environment.class);
            RotationLocker locker = appContext.getBean(RotationLocker.class);
            locker.clearCache();
            destroyAndCreateEmptySchema(oper, env);
        }

        void destroyAndCreateEmptySchema(JdbcOperations oper, Environment env) {
            if (env.acceptsProfiles(Profiles.of("postgres"))) {
                oper.update("DROP SCHEMA IF EXISTS keyrotation CASCADE");
            } else if (env.acceptsProfiles(Profiles.of("h2"))) {
                oper.update("DROP ALL OBJECTS DELETE FILES");
            } else {
                oper.update("DROP SCHEMA IF EXISTS keyrotation");
            }

            oper.update("CREATE SCHEMA keyrotation");
        }
    }
}
