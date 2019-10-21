package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyPair;

import java.security.KeyPair;

public interface EncryptingKeyGenerator {

    KeyPair generatePair(Encrypting fromTemplate);
    ProvidedKeyPair generate(Encrypting fromTemplate);
}
