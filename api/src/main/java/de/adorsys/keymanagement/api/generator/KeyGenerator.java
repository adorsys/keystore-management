package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Pbe;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyPair;

/**
 * Aggregate interface that provides all key generation related operations.
 */
public interface KeyGenerator {

    /**
     * Generates raw secret key that can be stored almost as is in keystore from String or char[].
     * To decode its value when you read it from keystore use {@link de.adorsys.keymanagement.api.source.KeyDecoder}
     * @param template To generate secret from.
     * @return Generated secret
     */
    ProvidedKey secretRaw(Pbe template);

    /**
     * Generates derived secret key from String or char[] - you will not be able to read original key, but you
     * can use it as initialization parameter for encryption.
     * @param template To generate secret from.
     * @return Generated secret
     */
    ProvidedKey secret(Pbe template);

    /**
     * Generates secret key to be used with certain encryptor and with certain size.
     * @param template Key template to generate from.
     * @return Generated secret
     */
    ProvidedKey secret(Secret template);

    /**
     * Generates public(certificate)-private key pair suitable for signing.
     * @param template  Key pair template to generate from.
     * @return Generated signing key pair.
     */
    ProvidedKeyPair signing(Signing template);

    /**
     * Generates public(certificate)-private key pair suitable for public-key encryption.
     * @param template  Key pair template to generate from.
     * @return Generated encryption key pair.
     */
    ProvidedKeyPair encrypting(Encrypting template);

    /**
     * Generates multiple keys from template at once.
     * @param template Key set template to generate from
     * @return Generated key set.
     */
    KeySet fromTemplate(KeySetTemplate template);
}
