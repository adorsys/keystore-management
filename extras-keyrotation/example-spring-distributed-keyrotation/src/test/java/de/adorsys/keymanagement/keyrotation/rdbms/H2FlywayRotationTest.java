package de.adorsys.keymanagement.keyrotation.rdbms;

import de.adorsys.keymanagement.keyrotation.BaseJdbcDbTest;
import de.adorsys.keymanagement.keyrotation.PopRotationValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "h2")
class H2FlywayRotationTest extends BaseJdbcDbTest {

    @Autowired
    private PopRotationValidator validator;

    @Test
    void testMigratesAndPopRotates() {
        validator.testPopRotates();
    }
}
