package de.adorsys.keymanagement.generator.types;


import de.adorsys.keymanagement.generator.deprecated.types.keystore.KeyEntry;

public interface KeyPairEntry extends KeyEntry {
    SelfSignedKeyPairData getKeyPair();
}
