package de.adorsys.keymanagement.template;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class KeyTemplate {

    private final String alias;

    public static SecretKey.SecretKeyBuilder secret() {
        return SecretKey.builder();
    }

    public static SigningKeyPair.SigningKeyPairBuilder signing() {
        return SigningKeyPair.builder();
    }

    public static EncryptingKeyPair.EncryptingKeyPairBuilder encrypting() {
        return EncryptingKeyPair.builder();
    }
}
