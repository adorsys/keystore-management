package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.template.generated.Pbe;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;

/**
 * Specific interface for generating secret keys
 */
public interface SecretKeyGenerator {

    /**
     * Generates key from template
     * @param fromTemplate Template that defines encrypting key properties like encryption algorithm, key size,
     *                     key name that are going to be used on creation
     * @return generated key
     */
    ProvidedKey generate(Pbe fromTemplate);

    /**
     * Generates key from template using default salt and iterations count
     * @param fromTemplate Template that defines encrypting key properties like encryption algorithm, key size,
     *                     key name that are going to be used on creation
     * @return generated key
     */
    ProvidedKey generateRaw(Pbe fromTemplate);

    /**
     * Generates key from Secret template
     * @param fromTemplate Template that defines encrypting key properties like encryption algorithm, key size,
     *                     key name that are going to be used on creation
     * @return generated key
     */
    ProvidedKey generate(Secret fromTemplate);
}
