package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;

import javax.crypto.SecretKey;
import java.util.function.Supplier;

public interface SecretKeyGenerator {

    SecretKey generateKey(Secret fromTemplate);
    SecretKey generateFromPassword(Supplier<char[]> password);
    ProvidedKey generate(Secret fromTemplate);
}
