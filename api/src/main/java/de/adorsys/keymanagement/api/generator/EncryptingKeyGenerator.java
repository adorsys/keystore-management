package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;

/**
 * Specific interface for generating encryption keys
 */
public interface EncryptingKeyGenerator {

    /**
     * generates key pair using template
     * @param fromTemplate Template that defines encrypting key properties like encryption algorithm, key size,
     *                     key name that are going to be used on creation
     * @return generated key pair
     */
    KeyPairEntry generate(Encrypting fromTemplate);
}
