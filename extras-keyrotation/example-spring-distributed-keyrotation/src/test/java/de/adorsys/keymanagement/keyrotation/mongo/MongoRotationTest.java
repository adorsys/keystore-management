package de.adorsys.keymanagement.keyrotation.mongo;

import de.adorsys.keymanagement.keyrotation.BaseMongoDbTest;
import de.adorsys.keymanagement.keyrotation.BaseSpringTest;
import de.adorsys.keymanagement.keyrotation.PopRotationValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "mongo")
class MongoRotationTest extends BaseMongoDbTest {

    @Autowired
    private PopRotationValidator validator;

    @Test
    void testPopRotates() {
        validator.testPopRotates();
    }
}
