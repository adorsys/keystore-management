package de.adorsys.keymanagement.keyrotation.service;

import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyRotator {

    private final RotatedKeyStore rotatedKeyStore;

    @Scheduled(cron = "${rotation.schedule}")
    void rotate() {
        rotatedKeyStore.rotation().rotate();
    }
}
