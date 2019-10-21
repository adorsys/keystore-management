package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;

import javax.crypto.SecretKey;

public interface SecretKeyGenerator {

    SecretKey generateKey(Secret fromTemplate);
    ProvidedKey generate(Secret fromTemplate);
}
