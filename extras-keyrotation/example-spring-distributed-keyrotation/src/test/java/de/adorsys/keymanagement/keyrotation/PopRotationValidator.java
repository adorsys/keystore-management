package de.adorsys.keymanagement.keyrotation;

import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.config.KeyRotation;
import de.adorsys.keymanagement.keyrotation.config.properties.RotationConfiguration;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Service
public class PopRotationValidator {

    @Autowired
    private RotationConfiguration conf;

    @Autowired
    private RotatedKeyStore rotatedKeyStore;

    @Autowired
    private KeyRotation.ResettableClock clock;

    @SneakyThrows
    public void testPopRotates() {
        assertThat(rotatedKeyStore.keys().keyStore()).isNull();

        rotatedKeyStore.forRotation().rotate();
        hasOnlyValid();

        clock.setInstant(Instant.now().plus(conf.getValidity()).plusSeconds(1));
        rotatedKeyStore.forRotation().rotate();
        hasValidAndLegacy();
    }

    private void hasValidAndLegacy() {
        conf.getCountValidByType().forEach((type, size) -> {
            assertThat(
                    rotatedKeyStore.keys()
                            .withValidity(ImmutableMap.of(type, ImmutableSet.of(KeyStatus.VALID))).all()
            ).hasSize(size);

            assertThat(
                    rotatedKeyStore.keys()
                            .withValidity(ImmutableMap.of(type, ImmutableSet.of(KeyStatus.LEGACY))).all()
            ).hasSize(size);
        });
    }

    private void hasOnlyValid() {
        conf.getCountValidByType().forEach((type, size) -> {
            assertThat(
                    rotatedKeyStore.keys()
                            .withValidity(ImmutableMap.of(type, ImmutableSet.of(KeyStatus.VALID))).all()
            ).hasSize(size);

            assertThat(
                    rotatedKeyStore.keys()
                            .withValidity(ImmutableMap.of(type, ImmutableSet.of(KeyStatus.LEGACY))).all()
            ).hasSize(0);
        });
    }
}
