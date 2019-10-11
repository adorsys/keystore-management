package de.adorsys.keymanagement.core.view.entity;

import de.adorsys.keymanagement.core.collection.keystore.KeyMetadata;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;

public class KeyPair extends KeyAlias {

    private final ProvidedKeyPair pair;

    public KeyPair(String alias, KeyMetadata meta, ProvidedKeyPair pair) {
        super(alias, meta);
        this.pair = pair;
    }
}
