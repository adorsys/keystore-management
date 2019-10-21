package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyPair;

import java.security.KeyPair;

public interface SigningKeyGenerator {

    KeyPair generatePair(Signing fromTemplate);
    ProvidedKeyPair generate(Signing fromTemplate);
}
