package de.adorsys.keymanagement.api.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

import java.security.KeyStore;

public interface KeyMetadataOper {

    default char[] metadataPassword(String forAlias) {
        return null;
    }

    String metadataAliasForKeyAlias(String forAlias);
    boolean isMetadataEntry(String forAlias, KeyStore keyStore);
    KeyMetadata extract(String forAlias, KeyStore keyStore);
    void persistMetadata(String forAlias, KeyMetadata metadata, KeyStore keyStore);
    void removeMetadata(String forAlias, KeyStore keyStore);
}
