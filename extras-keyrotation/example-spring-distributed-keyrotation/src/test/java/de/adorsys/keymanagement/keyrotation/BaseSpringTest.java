package de.adorsys.keymanagement.keyrotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test that assumes we do not do REST calls focusing on pure internals
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {PopRotationValidator.class, Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public abstract class BaseSpringTest extends BaseMockitoTest {
}
