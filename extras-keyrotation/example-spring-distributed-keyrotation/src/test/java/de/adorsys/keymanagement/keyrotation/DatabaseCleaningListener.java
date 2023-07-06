package de.adorsys.keymanagement.keyrotation;

import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * This listener cleanups DB after each test and performs migration on clean database before each test, this
 * trick allows us not to spin-up new DB instances between test classes.
 */
public class DatabaseCleaningListener extends AbstractTestExecutionListener {

    // This value ensures that Flyway migration will run before @Sql listener and @Transactional listener
    static final int ORDER = 3000;

    private static final String POSTGRESQL = "POSTGRESQL";
    private static final String H2 = "H2";
    private static final String MYSQL = "MYSQL";
    private static final Map<String, String> DROP_STATEMENTS_BY_DB_TYPE = ImmutableMap.of(
            POSTGRESQL, "DROP SCHEMA IF EXISTS keyrotation CASCADE",
            H2, "DROP ALL OBJECTS DELETE FILES",
            MYSQL, "DROP SCHEMA IF EXISTS keyrotation CASCADE"
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
    public void afterTestMethod(TestContext testContext) throws SQLException {
        System.out.println("Dropping database");
        ApplicationContext appContext = testContext.getApplicationContext();
        JdbcOperations oper = appContext.getBean(JdbcOperations.class);
        RotationLocker locker = appContext.getBean(RotationLocker.class);
        locker.clearCache();
        destroyAndCreateEmptySchema(appContext, oper);
    }

    private void destroyAndCreateEmptySchema(ApplicationContext context, JdbcOperations oper) throws SQLException {
        DataSource dataSource = (DataSource) context.getBean("dataSource");
        Connection connection = dataSource.getConnection();
        final String databaseType = connection.getMetaData().getDatabaseProductName();
        String dropStatement = DROP_STATEMENTS_BY_DB_TYPE.getOrDefault(
                databaseType,
                DROP_STATEMENTS_BY_DB_TYPE.get(MYSQL)
        );
        oper.execute(dropStatement);

        oper.update("CREATE SCHEMA keyrotation");
    }
}
