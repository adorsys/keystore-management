package de.adorsys.keymanagement.keyrotation.controller;

import de.adorsys.keymanagement.keyrotation.Application;
import de.adorsys.keymanagement.keyrotation.BaseMockitoTest;
import de.adorsys.keymanagement.keyrotation.DatabaseCleaningListener;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // Performance optimization
@ImportAutoConfiguration(exclude = {
        EmbeddedMongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        MongoAutoConfiguration.class
})
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestExecutionListeners(value = DatabaseCleaningListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class BaseEndpointTest extends BaseMockitoTest {
}
