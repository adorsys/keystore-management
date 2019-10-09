package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;

import java.security.KeyPair;

public interface SigningKeyGenerator {

    KeyPair generatePair(Signing fromTemplate);
    ProvidedKeyPair generate(Signing fromTemplate);
}
