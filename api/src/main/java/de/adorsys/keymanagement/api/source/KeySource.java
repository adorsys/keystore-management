package de.adorsys.keymanagement.api.source;

import de.adorsys.keymanagement.api.types.entity.WithMetadata;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.stream.Stream;

public interface KeySource {

    Stream<WithMetadata<String>> aliases();

    // T acts as a type-selector so can be safely used for KeyStore too
    <T extends ProvidedKeyTemplate> Stream<WithMetadata<String>> aliasesFor(Class<T> clazz);

    WithMetadata<String> asAliasWithMeta(String alias);
    WithMetadata<KeyStore.Entry> asEntry(String alias);
    WithMetadata<KeyPair> asPair(String alias);
    WithMetadata<Key> asKey(String alias);

    void remove(String keyId);
    String addAndReturnId(ProvidedKeyTemplate keyTemplate);
}
