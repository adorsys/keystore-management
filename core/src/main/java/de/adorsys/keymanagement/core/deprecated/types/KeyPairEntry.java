package de.adorsys.keymanagement.core.deprecated.types;


import de.adorsys.keymanagement.core.deprecated.types.keystore.KeyEntry;

public interface KeyPairEntry extends KeyEntry {
    SelfSignedKeyPairData getKeyPair();
}
