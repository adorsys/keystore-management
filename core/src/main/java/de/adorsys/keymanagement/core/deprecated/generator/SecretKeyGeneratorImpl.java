package de.adorsys.keymanagement.core.deprecated.generator;

import de.adorsys.keymanagement.core.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.core.deprecated.types.keystore.SecretKeyGenerator;

import javax.crypto.SecretKey;

public class SecretKeyGeneratorImpl implements SecretKeyGenerator {

    private final String secretKeyAlgo;
    private final Integer keySize;

    public SecretKeyGeneratorImpl(String secretKeyAlgo, Integer keySize) {
        this.secretKeyAlgo = secretKeyAlgo;
        this.keySize = keySize;
    }

    @Override
    public SecretKeyData generate(String alias, ReadKeyPassword readKeyPassword) {
        SecretKey secretKey = new SecretKeyBuilder()
                .withKeyAlg(secretKeyAlgo)
                .withKeyLength(keySize)
                .build();

        return SecretKeyData.builder().secretKey(secretKey).alias(alias).readKeyPassword(readKeyPassword).keyAlgo(secretKeyAlgo).build();

    }
}
