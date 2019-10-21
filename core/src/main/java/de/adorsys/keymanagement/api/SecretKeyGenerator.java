package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.types.template.generated.Secret;
import de.adorsys.keymanagement.core.types.template.provided.ProvidedKey;

import javax.crypto.SecretKey;

public interface SecretKeyGenerator {

    SecretKey generateKey(Secret fromTemplate);
    ProvidedKey generate(Secret fromTemplate);
}
