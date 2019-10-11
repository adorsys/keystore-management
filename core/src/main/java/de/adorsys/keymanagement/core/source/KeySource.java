package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;

import java.util.stream.Stream;

public abstract class KeySource {

    public abstract Stream<String> aliases();
    public abstract <T extends ProvidedKeyTemplate> Stream<String> aliasesFor(Class<T> clazz);
    public abstract ProvidedKeyEntry asEntry(String alias);
    public abstract ProvidedKeyPair asPair(String alias);
    public abstract ProvidedKey asKey(String alias);

    public abstract void remove(String keyId);
    public abstract String addAndReturnId(ProvidedKeyTemplate keyTemplate);
}
