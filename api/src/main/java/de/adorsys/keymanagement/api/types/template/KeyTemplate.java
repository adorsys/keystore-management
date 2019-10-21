package de.adorsys.keymanagement.api.types.template;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

import java.util.function.Supplier;

public interface KeyTemplate {

    String generateName();
    Supplier<char[]> getPassword();
    KeyMetadata getMetadata();
}
