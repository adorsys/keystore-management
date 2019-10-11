package de.adorsys.keymanagement.core.view.entity;

import de.adorsys.keymanagement.core.collection.keystore.KeyMetadata;
import de.adorsys.keymanagement.core.template.provided.ProvidedKey;

public class Key extends KeyAlias {

    private final ProvidedKey key;

    public Key(String alias, KeyMetadata meta, ProvidedKey key) {
        super(alias, meta);
        this.key = key;
    }
}
