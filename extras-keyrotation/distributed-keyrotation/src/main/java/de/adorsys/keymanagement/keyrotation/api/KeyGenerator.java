package de.adorsys.keymanagement.keyrotation.api;

import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;

public interface KeyGenerator {

    ProvidedKeyTemplate generateValidKey(KeyType forType);
}
