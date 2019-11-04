package de.adorsys.keymanagement.keyrotation;

import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

@Service
public class PopRotationValidator {

    @Autowired
    private RotatedKeyStore rotatedKeyStore;

    @SneakyThrows
    public void testPopRotates() {
        assertThat(rotatedKeyStore.keys().keyStore()).isNull();

        rotatedKeyStore.rotation();
    }
}
