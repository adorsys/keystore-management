package de.adorsys.keymanagement.core.view;

import de.adorsys.keymanagement.core.source.KeySource;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.val;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class UpdatingView<V> implements View<V> {

    public boolean update(Collection<V> objectsToRemove, Collection<ProvidedKeyTemplate> objectsToAdd) {
        KeySource source = getSource();
        objectsToRemove.forEach(it -> source.remove(getKeyId(it)));
        val newKeys = objectsToAdd.stream()
                .map(source::addAndReturnId)
                .map(this::viewFromId)
                .collect(Collectors.toList());
        return updateCollection(objectsToRemove, newKeys);
    }

    protected abstract KeySource getSource();
    protected abstract String getKeyId(V ofKey);
    protected abstract V viewFromId(String ofKey);
    protected abstract boolean updateCollection(Collection<V> keysToRemove, Collection<V> keysToAdd);
}
