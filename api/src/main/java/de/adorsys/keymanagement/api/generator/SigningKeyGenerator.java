package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.template.generated.Signing;

/**
 * Specific interface for generating signing keys
 */
public interface SigningKeyGenerator {

    /**
     * generates key pair using template
     * @param fromTemplate encrypting template which is used to generate key pair
     * @return generated key pair(private and public keys)
     */
    KeyPairEntry generate(Signing fromTemplate);
}
