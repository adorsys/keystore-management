package de.adorsys.keymanagement.deprecated.types;


import de.adorsys.keymanagement.deprecated.types.keystore.KeyEntry;

public interface KeyPairEntry extends KeyEntry {
    SelfSignedKeyPairData getKeyPair();
}
