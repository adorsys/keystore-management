package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types;


import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.keystore.KeyEntry;

public interface KeyPairEntry extends KeyEntry {
    SelfSignedKeyPairData getKeyPair();
}
