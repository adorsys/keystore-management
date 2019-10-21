package de.adorsys.keymanagement.core.types.entity;

import de.adorsys.keymanagement.core.types.entity.metadata.KeyMetadata;
import lombok.Getter;

import java.security.KeyStore;

@Getter
public class KeyEntry extends KeyAlias {

    private final KeyStore.Entry entry;

    public KeyEntry(String alias, KeyMetadata meta, KeyStore.Entry entry) {
        super(alias, meta);
        this.entry = entry;
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
