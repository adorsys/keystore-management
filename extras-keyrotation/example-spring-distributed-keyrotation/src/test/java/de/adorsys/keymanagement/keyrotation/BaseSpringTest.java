package de.adorsys.keymanagement.keyrotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test that assumes we do not do REST calls focusing on pure internals
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {Application.class, PopRotationValidator.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ImportAutoConfiguration(exclude = {
        EmbeddedMongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        MongoAutoConfiguration.class
})
public abstract class BaseSpringTest extends BaseMockitoTest {
}
