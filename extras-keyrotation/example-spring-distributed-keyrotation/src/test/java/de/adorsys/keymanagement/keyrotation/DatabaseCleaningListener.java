package de.adorsys.keymanagement.keyrotation;

import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.jdbc.DatabaseType;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * This listener cleanups DB after each test and performs migration on clean database before each test, this
 * trick allows us not to spin-up new DB instances between test classes.
 */
public class DatabaseCleaningListener extends AbstractTestExecutionListener {

    // This value ensures that Flyway migration will run before @Sql listener and @Transactional listener
    static final int ORDER = 3000;

    private static final Map<DatabaseType, String> DROP_STATEMENTS_BY_DB_TYPE = ImmutableMap.of(
            DatabaseType.POSTGRESQL, "DROP SCHEMA IF EXISTS keyrotation CASCADE",
            DatabaseType.H2, "DROP ALL OBJECTS DELETE FILES",
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
        System.out.println("Migrating database with flyway");
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
        System.out.println("Dropping database");
        ApplicationContext appContext = testContext.getApplicationContext();
        JdbcOperations oper = appContext.getBean(JdbcOperations.class);
        RotationLocker locker = appContext.getBean(RotationLocker.class);
        locker.clearCache();
        destroyAndCreateEmptySchema(oper);
    }

    private void destroyAndCreateEmptySchema(JdbcOperations oper) {
        DatabaseType type = oper.execute(DatabaseType::fromJdbcConnection);
        String dropStatement = DROP_STATEMENTS_BY_DB_TYPE.getOrDefault(
                type,
                DROP_STATEMENTS_BY_DB_TYPE.get(DatabaseType.MYSQL)
        );
        oper.execute(dropStatement);

        oper.update("CREATE SCHEMA keyrotation");
    }
}
