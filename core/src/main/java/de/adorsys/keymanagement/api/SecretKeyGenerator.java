package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.provided.Provided;

import javax.crypto.SecretKey;

public interface SecretKeyGenerator {

    SecretKey generateKey(Secret fromTemplate);
    Provided generate(Secret fromTemplate);
}
