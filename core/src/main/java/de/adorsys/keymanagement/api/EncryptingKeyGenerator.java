package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.types.template.generated.Encrypting;
import de.adorsys.keymanagement.core.types.template.provided.ProvidedKeyPair;

import java.security.KeyPair;

public interface EncryptingKeyGenerator {

    KeyPair generatePair(Encrypting fromTemplate);
    ProvidedKeyPair generate(Encrypting fromTemplate);
}
