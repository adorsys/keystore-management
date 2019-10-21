package de.adorsys.keymanagement.api.types.entity;

import lombok.Getter;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
public class Key extends KeyAlias {

    private final java.security.Key key;

    public Key(String alias, WithMetadata<java.security.Key> key) {
        super(alias, key.getMetadata());
        this.key = key.getKey();
    }

    public boolean isSecret() {
        return key instanceof SecretKey;
    }

    public boolean isPrivate() {
        return key instanceof PrivateKey;
    }

    public boolean isPublic() {
        return key instanceof PublicKey;
    }
}
