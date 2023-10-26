package de.adorsys.keymanagement.api.types.entity;

import lombok.Getter;

import java.security.KeyStore;

@Getter
public class KeyEntry extends KeyAlias {

    private final KeyStore.Entry entry;
    private final boolean isSecret;
    private final boolean isPrivate;
    private final boolean isTrustedCert;

    public KeyEntry(String alias, WithMetadata<KeyStore.Entry> entry) {
        super(alias, entry.getMetadata(), entry.isMetadataEntry());
        this.entry = entry.getKey();
        isSecret = entry.getKey() instanceof KeyStore.SecretKeyEntry;
        isPrivate = entry.getKey() instanceof KeyStore.PrivateKeyEntry;
        isTrustedCert = entry.getKey() instanceof KeyStore.TrustedCertificateEntry;
    }
}
