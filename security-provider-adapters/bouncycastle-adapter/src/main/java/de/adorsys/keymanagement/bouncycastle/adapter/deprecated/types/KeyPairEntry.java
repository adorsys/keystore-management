package de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types;


import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore.KeyEntry;

public interface KeyPairEntry extends KeyEntry {
    SelfSignedKeyPairData getKeyPair();
}
