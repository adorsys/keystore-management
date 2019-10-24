package de.adorsys.keymanagement.api.types.entity;

import lombok.Getter;

@Getter
public class KeyPair extends KeyAlias {

    private final java.security.KeyPair pair;

    public KeyPair(String alias, WithMetadata<java.security.KeyPair> pair) {
        super(alias, pair.getMetadata(), pair.isMetadataEntry());
        this.pair = pair.getKey();
    }
}
