package de.adorsys.keymanagement.keyrotation.config;

import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.juggler.services.DaggerBCJuggler;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import de.adorsys.keymanagement.keyrotation.config.properties.RotationConfiguration;
import de.adorsys.keymanagement.keyrotation.services.DaggerRotatedKeyStore;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.Synchronized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Configuration
public class KeyRotation {

    @Bean
    ResettableClock clock() {
        return new ResettableClock();
    }

    /**
     * This is primary bean you need to have rotatable KeyStore.
     */
    @Bean
    RotatedKeyStore rotatedKeyStore(ResettableClock clock, RotationLocker locker, KeyStorePersistence persistence,
                                    RotationConfiguration config) {
        return DaggerRotatedKeyStore.builder()
                .distributedLock(locker)
                .persistence(persistence)
                .keyStoreConfig(KeyStoreConfig.builder().type(config.getKeystore().getType()).build()) // Setting to less secure keystore for demo purposes
                .timeSource(clock) // <- You don't need this in production code, it is mostly for demo, unless you need special clock
                .rotationConfig(config)
                .build();
    }

    /**
     * This clock allows to set time from REST API/tests to validate key expiration etc.
     * You don't need this in production code.
     */
    public static class ResettableClock extends Clock {

        private final Clock clock = systemUTC();
        private Instant instant;

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        @Synchronized
        public Instant instant() {
            return null == instant ? clock.instant() : instant;
        }

        @Synchronized
        public void setInstant(Instant instant) {
            this.instant = instant;
        }
    }
}
