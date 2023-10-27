package de.adorsys.keymanagement.api.types.entity;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import lombok.Getter;

import java.security.KeyStore;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

@Getter
public class KeyEntry extends KeyAlias {

    private final KeyStore.Entry entry;
    private final boolean isSecret;
    private final boolean isPrivate;
    private final boolean isTrustedCert;

    public static final SimpleAttribute<KeyEntry, ?> IS_SECRET =
            attribute(KeyEntry.class, Boolean.class, "is_secret", KeyEntry::isSecret);
    public static final SimpleAttribute<KeyEntry, ?> IS_PRIVATE =
            attribute(KeyEntry.class, Boolean.class, "is_private", KeyEntry::isPrivate);
    public static final SimpleAttribute<KeyEntry, ?> IS_TRUSTED_CERT =
            attribute(KeyEntry.class, Boolean.class, "is_trusted_cert", KeyEntry::isTrustedCert);
    public static final SimpleAttribute<KeyEntry, ?> ALIAS =
            attribute(KeyEntry.class, String.class, "alias", KeyEntry::getAlias);

    public KeyEntry(String alias, WithMetadata<KeyStore.Entry> entry) {
        super(alias, entry.getMetadata(), entry.isMetadataEntry());
        this.entry = entry.getKey();
        isSecret = entry.getKey() instanceof KeyStore.SecretKeyEntry;
        isPrivate = entry.getKey() instanceof KeyStore.PrivateKeyEntry;
        isTrustedCert = entry.getKey() instanceof KeyStore.TrustedCertificateEntry;
    }
}
