package de.adorsys.keymanagement.keyrotation.api.types;

import de.adorsys.keymanagement.api.types.template.GeneratedKeyTemplate;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public interface KeyRotationConfig {

    /**
     * Required amount of `Valid` (usable both for encryption/decryption) in KeyStore per each type.
     */
    Map<KeyType, Integer> getCountValidByType();

    /**
     * For which key types rotation is enabled.
     */
    Collection<KeyType> getEnabledFor();

    /**
     * Which algorithms are associated with a key - i.e. Secret key for AES-256 encryption.
     */
    Map<KeyType, GeneratedKeyTemplate> getKeyTemplate();

    /**
     * For how long key should be valid (used for encryption/decryption) from the moment it appeared in KeyStore.
     */
    Duration getValidity();

    /**
     * For how long key can be used for decryption/signature validation from the moment it appeared in KeyStore.
     */
    Duration getLegacy();

    /**
     * Key password protection within KeyStore.
     */
    Supplier<char[]> keyPassword();

    /**
     * KeyStore password protection.
     */
    Supplier<char[]> keyStorePassword();
}
