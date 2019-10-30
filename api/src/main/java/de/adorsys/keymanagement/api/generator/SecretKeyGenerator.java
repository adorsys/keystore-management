package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.template.generated.Pbe;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;

public interface SecretKeyGenerator {

    ProvidedKey generate(Pbe fromTemplate);
    ProvidedKey generateRaw(Pbe fromTemplate);
    ProvidedKey generate(Secret fromTemplate);
}
