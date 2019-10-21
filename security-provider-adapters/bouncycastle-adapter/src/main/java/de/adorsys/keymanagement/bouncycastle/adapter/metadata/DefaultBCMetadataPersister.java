package de.adorsys.keymanagement.bouncycastle.adapter.metadata;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class DefaultBCMetadataPersister implements BCMetadataPersister {

    @Inject
    public DefaultBCMetadataPersister() {
    }

    @Override
    public KeyMetadata extract(String forAlias) {
        return null;
    }

    @Override
    public void persistMetadata(String forAlias, KeyMetadata metadata) {

    }

    @Override
    public void removeMetadata(String forAlias) {

    }
}
