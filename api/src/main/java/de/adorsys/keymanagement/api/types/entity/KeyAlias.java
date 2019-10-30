package de.adorsys.keymanagement.api.types.entity;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KeyAlias {

    private final String alias;
    private final KeyMetadata meta;

    /**
     * Indicates whether given alias points not to a true key, but to key metadata.
     */
    private final boolean metadataEntry;

    public <T extends KeyMetadata> AliasWithMeta<T> aliasWithMeta(Class<T> metadataClass) {
        if (metadataEntry) {
            throw new IllegalStateException("Entry is metadata itself");
        }

        return new AliasWithMeta<>(getAlias(), getMeta(metadataClass));
    }

    public <T> T getMeta(Class<T> asClass) {
        return (T) meta;
    }
}
