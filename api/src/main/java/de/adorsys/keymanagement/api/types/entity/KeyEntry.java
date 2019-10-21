package de.adorsys.keymanagement.api.types.entity;

import lombok.Getter;

import java.security.KeyStore;

@Getter
public class KeyEntry extends KeyAlias {

    private final KeyStore.Entry entry;

    public KeyEntry(String alias, WithMetadata<KeyStore.Entry> entry) {
        super(alias, entry.getMetadata());
        this.entry = entry.getKey();
    }

    public boolean isSecret() {
        return entry instanceof KeyStore.SecretKeyEntry;
    }

    public boolean isPrivate() {
        return entry instanceof KeyStore.PrivateKeyEntry;
    }

    public boolean isTrustedCert() {
        return entry instanceof KeyStore.TrustedCertificateEntry;
    }
}
