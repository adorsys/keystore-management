package de.adorsys.keymanagement.keyrotation.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Key rotation scheduler.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty("rotation.schedule")
public class SchedulingConfig {
}
