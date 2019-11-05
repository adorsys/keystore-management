package de.adorsys.keymanagement.keyrotation.api.services;

import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;

import java.time.Instant;

public interface KeyGenerator {

    ProvidedKeyTemplate generateValidKey(Instant now, KeyType forType);
}
