package de.adorsys.keymanagement.core.metadata;

import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

/**
 * Trivial metadata persistence strategy using surrogate key alias - key metadata is persisted by composing
 * key id and adding some suffix {@link ToKeyStoreMetadataPersister#METADATA_SUFFIX} to it. Payload is stored in
 * key entry as JSON-entity.
 */
@Slf4j
public class ToKeyStoreMetadataPersister implements MetadataPersister {

    private static final String METADATA_SUFFIX = "-KEY-METADATA";

    private final MetadataPersistenceConfig persistenceConfig;
    private final SecretKeyGenerator secretKeyGenerator;

    @Inject
    public ToKeyStoreMetadataPersister(
            @Nullable MetadataPersistenceConfig persistenceConfig, SecretKeyGenerator secretKeyGenerator) {
        /*
         * Should not be nullable, {@link MetadataModule} should provide this class only if {@code persistenceConfig}
         * is not null.
         */
        if (null == persistenceConfig) {
            throw new IllegalStateException("Metadata persistence can't be null");
        }
        this.persistenceConfig = persistenceConfig;
        this.secretKeyGenerator = secretKeyGenerator;
    }

    @Override
    @SneakyThrows
    public KeyMetadata extract(String forAlias, KeyStore keyStore) {
        if (forAlias.endsWith(METADATA_SUFFIX)) { // FIXME collision prone - i.e. key-name + md5(key-name) + suffix is more collision resilent
            return null;
        }

        val metadata = new String(
                keyStore.getKey(metadataForKeyAlias(forAlias), null).getEncoded(),
                StandardCharsets.UTF_8
        );

        return persistenceConfig.getGson().fromJson(metadata, persistenceConfig.getMetadataClass());
    }

    @Override
    @SneakyThrows
    public void persistMetadata(String forAlias, KeyMetadata metadata, KeyStore keyStore) {
        String value = persistenceConfig.getGson().toJson(metadata);
        keyStore.setKeyEntry(
                metadataForKeyAlias(forAlias),
                secretKeyGenerator.generateFromPassword(() -> value.toCharArray()), // FIXME - lighter encryption?
                null,
                null
        );
    }

    private String metadataForKeyAlias(String forAlias) {
        return forAlias + METADATA_SUFFIX;
    }

    @Override
    @SneakyThrows
    public void removeMetadata(String forAlias, KeyStore keyStore) {
        keyStore.deleteEntry(forAlias);
    }
}
