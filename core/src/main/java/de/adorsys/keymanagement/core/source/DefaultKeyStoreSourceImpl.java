package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.keystore.KeyStoreOper;
import de.adorsys.keymanagement.api.persist.KeyMetadataPersister;
import de.adorsys.keymanagement.api.source.KeyMetadataExtractor;
import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.entity.WithMetadata;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyEntry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DefaultKeyStoreSourceImpl implements KeySource {

    private final KeyMetadataExtractor extractor;
    private final KeyMetadataPersister persister;
    private final KeyStore store;
    private final KeyStoreOper oper;
    private final Function<String, char[]> keyPassword; // used to read

    @Inject
    public DefaultKeyStoreSourceImpl(
            @Nullable KeyMetadataExtractor extractor,
            @Nullable KeyMetadataPersister persister,
            KeyStore store, KeyStoreOper oper, Function<String, char[]> keyPassword) {
        this.extractor = extractor;
        this.persister = persister;
        this.store = store;
        this.oper = oper;
        this.keyPassword = keyPassword;
    }

    @Override
    @SneakyThrows
    public Stream<WithMetadata<String>> aliases() {
        Enumeration<String> aliases = store.aliases();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new EnumerationToIterator(aliases, extractor),
                        Spliterator.ORDERED
                ), false
        );
    }

    @Override
    public <T extends ProvidedKeyTemplate> Stream<WithMetadata<String>> aliasesFor(Class<T> clazz) {
        if (!clazz.equals(ProvidedKeyEntry.class)) {
            return Stream.empty();
        }

        return aliases();
    }

    @Override
    public WithMetadata<String> asAliasWithMeta(String alias) {
        return WithMetadata.<String>builder()
                .key(alias)
                .metadata(extractor != null ? extractor.extract(alias) : null)
                .build();
    }

    @Override
    @SneakyThrows
    public WithMetadata<KeyStore.Entry> asEntry(String alias) {
        return WithMetadata.<KeyStore.Entry>builder()
                .key(store.getEntry(alias, new KeyStore.PasswordProtection(keyPassword.apply(alias))))
                .metadata(extractor != null ? extractor.extract(alias) : null)
                .build();
    }

    @Override
    public WithMetadata<KeyPair> asPair(String alias) {
        throw new IllegalStateException("Prohibited");
    }

    @Override
    public WithMetadata<Key> asKey(String alias) {
        throw new IllegalStateException("Prohibited");
    }

    @Override
    @SneakyThrows
    public void remove(String keyId) {
        store.deleteEntry(keyId);
        if (null != persister) {
            persister.removeMetadata(keyId);
        }
    }

    @Override
    @SneakyThrows
    public String addAndReturnId(ProvidedKeyTemplate keyTemplate) {
        String alias = oper.addToKeyStoreAndGetName(store, keyTemplate, () -> null);
        if (null != persister) {
            persister.persistMetadata(alias, keyTemplate.getMetadata());
        }
        return alias;
    }

    @RequiredArgsConstructor
    private static class EnumerationToIterator implements Iterator<WithMetadata<String>> {

        private final Enumeration<String> source;
        private final KeyMetadataExtractor extractor;

        @Override
        public boolean hasNext() {
            return source.hasMoreElements();
        }

        @Override
        public WithMetadata<String> next() {
            String next = source.nextElement();

            return WithMetadata.<String>builder()
                    .key(next)
                    .metadata(null != extractor ? extractor.extract(next) : null)
                    .build();
        }
    }
}
