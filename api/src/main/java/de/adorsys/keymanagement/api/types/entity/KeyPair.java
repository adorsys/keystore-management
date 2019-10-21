package de.adorsys.keymanagement.api.types.entity;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.Getter;

@Getter
public class KeyPair extends KeyAlias {

    private final java.security.KeyPair pair;

    public KeyPair(String alias, KeyMetadata meta, java.security.KeyPair pair) {
        super(alias, meta);
        this.pair = pair;
    }
}
