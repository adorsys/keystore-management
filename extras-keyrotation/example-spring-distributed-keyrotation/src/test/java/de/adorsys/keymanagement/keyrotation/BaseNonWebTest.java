package de.adorsys.keymanagement.keyrotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springdoc.config.SpringDocConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test that assumes we do not do REST calls focusing on pure internals
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // Performance optimization
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {PopRotationValidator.class, Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ImportAutoConfiguration(exclude = {
        SpringDocConfiguration.class, // Enables WebMvc we don't need it
})
public abstract class BaseNonWebTest extends BaseMockitoTest {
}
