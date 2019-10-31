package de.adorsys.keymanagement.keyrotation.api;

import de.adorsys.keymanagement.api.types.template.GeneratedKeyTemplate;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public interface KeyRotationConfig {

    Map<KeyType, Integer> getKeysByType();
    Collection<KeyType> getEnabledFor();
    Map<KeyType, GeneratedKeyTemplate> getKeyTemplate();
    Duration getValidity();
    Duration getLegacy();
    Supplier<char[]> getKeyPassword();
}
