package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;

/**
 * Specific interface for generating encryption keys
 */
public interface EncryptingKeyGenerator {

    /**
     * generates key pair using template
     * @param fromTemplate encrypting template which is used to generate key pair
     * @return generated key pair
     */
    KeyPairEntry generate(Encrypting fromTemplate);
}
