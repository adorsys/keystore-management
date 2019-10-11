package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.stream.Stream;

public abstract class KeySource {

    public abstract Stream<String> aliases();

    // T acts as a type-selector so can be safely used for KeyStore too
    public abstract <T extends ProvidedKeyTemplate> Stream<String> aliasesFor(Class<T> clazz);

    public abstract KeyStore.Entry asEntry(String alias);
    public abstract KeyPair asPair(String alias);
    public abstract Key asKey(String alias);

    public abstract void remove(String keyId);
    public abstract String addAndReturnId(ProvidedKeyTemplate keyTemplate);
}
