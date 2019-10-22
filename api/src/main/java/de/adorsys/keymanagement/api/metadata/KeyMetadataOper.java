package de.adorsys.keymanagement.api.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;

import java.security.KeyStore;

public interface KeyMetadataOper {

    KeyMetadata extract(String forAlias, KeyStore keyStore);
    void persistMetadata(String forAlias, KeyMetadata metadata, KeyStore keyStore);
    void removeMetadata(String forAlias, KeyStore keyStore);
}
