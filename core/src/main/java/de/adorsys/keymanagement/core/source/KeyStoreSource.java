package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class KeyStoreSource implements KeySource {

    private final KeyStoreOper oper;
    private final KeyStore store;
    private final Function<String, char[]> keyPassword;

    @Override
    @SneakyThrows
    public Stream<String> aliases() {
        Enumeration<String> aliases = store.aliases();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new EnumerationToIterator<>(aliases),
                        Spliterator.ORDERED
                ), false
        );
    }

    @Override
    public <T extends ProvidedKeyTemplate> Stream<String> aliasesFor(Class<T> clazz) {
        if (!clazz.equals(ProvidedKeyEntry.class)) {
            return Stream.empty();
        }

        return aliases();
    }

    @Override
    @SneakyThrows
    public KeyStore.Entry asEntry(String alias) {
        return store.getEntry(alias, new KeyStore.PasswordProtection(keyPassword.apply(alias)));
    }

    @Override
    public KeyPair asPair(String alias) {
        throw new IllegalStateException("Prohibited");
    }

    @Override
    public Key asKey(String alias) {
        throw new IllegalStateException("Prohibited");
    }

    @Override
    @SneakyThrows
    public void remove(String keyId) {
        store.deleteEntry(keyId);
    }

    @Override
    @SneakyThrows
    public String addAndReturnId(ProvidedKeyTemplate keyTemplate) {
        return oper.addToKeyStoreAndGetName(store, keyTemplate);
    }

    @RequiredArgsConstructor
    private static class EnumerationToIterator<T> implements Iterator<T> {

        private final Enumeration<T> source;

        @Override
        public boolean hasNext() {
            return source.hasMoreElements();
        }

        @Override
        public T next() {
            return source.nextElement();
        }
    }
}
