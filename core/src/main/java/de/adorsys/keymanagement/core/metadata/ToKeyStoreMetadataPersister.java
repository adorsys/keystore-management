package de.adorsys.keymanagement.core.metadata;

import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.security.KeyStore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Trivial metadata persistence strategy using surrogate key alias - key metadata is persisted by composing
 * key id and adding some suffix {@link ToKeyStoreMetadataPersister#METADATA_SUFFIX} to it. Payload is stored in
 * key entry as JSON-entity.
 */
@Slf4j
public class ToKeyStoreMetadataPersister implements MetadataPersister {

    private static final String METADATA_SUFFIX = "KEYMETADATA";
    private static final Pattern METADATA_PATTERN = Pattern.compile("(.+):([0-9A-F]+)-KEYMETADATA$");

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
    public boolean isMetadataEntry(String forAlias, KeyStore keyStore) {
        if (!forAlias.endsWith(METADATA_SUFFIX)) {
            return false;
        }

        Matcher matcher = METADATA_PATTERN.matcher(forAlias);
        if (!matcher.matches()) {
            return false;
        }
        String keyAlias = matcher.group(1);
        String crc32 = matcher.group(2);

        return crc32.equals(crc32(keyAlias));
    }

    @Override
    @SneakyThrows
    public KeyMetadata extract(String forAlias, KeyStore keyStore) {
        if (isMetadataEntry(forAlias, keyStore)) {
            return null;
        }

        String alias = metadataAliasForKeyAlias(forAlias);

        if (!keyStore.containsAlias(alias)) {
            return null;
        }

        val metadata = new String(
                keyStore.getKey(alias, null).getEncoded(),
                UTF_8
        );

        return persistenceConfig.getGson().fromJson(metadata, persistenceConfig.getMetadataClass());
    }

    @Override
    @SneakyThrows
    public void persistMetadata(String forAlias, KeyMetadata metadata, KeyStore keyStore) {
        String value = persistenceConfig.getGson().toJson(metadata);
        keyStore.setKeyEntry(
                metadataAliasForKeyAlias(forAlias),
                secretKeyGenerator.generateFromPassword(() -> value.toCharArray()), // FIXME - lighter encryption?
                null,
                null
        );
    }

    // To reduce collision probability metadata has alias `KEY_ID:crc32(KEY_ID)-KEYMETADATA`
    // This is highly unlikely that someone will persist same
    @Override
    public String metadataAliasForKeyAlias(String forAlias) {
        return forAlias + ":" + crc32(forAlias) + "-" + METADATA_SUFFIX;
    }

    // Reduces collision probability even if someone uses key with alias ending with `-KEY-METADATA`
    private String crc32(String forAlias) {
        CRC32 crc = new CRC32();
        crc.update(forAlias.getBytes(UTF_8));
        return Long.toHexString(crc.getValue()).toUpperCase();
    }

    @Override
    @SneakyThrows
    public void removeMetadata(String forAlias, KeyStore keyStore) {
        keyStore.deleteEntry(metadataAliasForKeyAlias(forAlias));
    }
}
