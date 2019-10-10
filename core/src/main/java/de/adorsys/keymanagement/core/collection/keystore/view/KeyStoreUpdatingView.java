package de.adorsys.keymanagement.core.collection.keystore.view;

import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.api.ModifiableView;
import de.adorsys.keymanagement.core.WithAlias;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.security.KeyStore;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class KeyStoreUpdatingView<I extends WithAlias, A extends ProvidedKeyTemplate, K> implements ModifiableView<I, A> {

    private final KeyStore source;
    private final KeyStoreOper oper;

    @Override
    public boolean update(Collection<I> objectsToRemove, Collection<A> objectsToAdd) {
        objectsToRemove.forEach(this::deleteEntryFromKeystore);
        Map<String, A> addedKeys = new HashMap<>();
        objectsToAdd.forEach(it -> addKey(addedKeys, it));

        return updateInternal(
                objectsToRemove,
                addedKeys.entrySet().stream().map(
                        it -> readByAlias(it.getKey(), it.getValue().getPassword())
                ).collect(Collectors.toList())
        );
    }

    protected abstract boolean updateInternal(Collection<I> toRemove, Collection<K> toAdd);
    protected abstract K readByAlias(String alias, Supplier<char[]> password);

    private void addKey(Map<String, A> addedKeys, A it) {
        if (it instanceof Provided) {
            addedKeys.put(oper.addToKeyStoreAndGetName(source, (Provided) it), it);
        } else if (it instanceof ProvidedKeyPair) {
            addedKeys.put(oper.addToKeyStoreAndGetName(source, (ProvidedKeyPair) it), it);
        } else if (it instanceof ProvidedKeyEntry) {
            addedKeys.put(oper.addToKeyStoreAndGetName(source, (ProvidedKeyEntry) it), it);
        } else {
            throw new IllegalArgumentException("Unknown type of key to add: " + it.getClass());
        }
    }

    @SneakyThrows
    private void deleteEntryFromKeystore(WithAlias toRemove) {
        source.deleteEntry(toRemove.getAlias());
    }
}
