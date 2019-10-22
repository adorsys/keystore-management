package de.adorsys.keymanagement.core.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.view.UpdatingView;
import lombok.val;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public abstract class BaseUpdatingView<O> implements UpdatingView<O> {

    public boolean add(ProvidedKeyTemplate objectToAdd) {
        return add(Collections.singleton(objectToAdd));
    }

    public boolean remove(O objectToRemove) {
        return remove(Collections.singleton(objectToRemove));
    }

    public boolean add(Collection<ProvidedKeyTemplate> objectsToAdd) {
        return update(Collections.emptyList(), objectsToAdd);
    }

    public boolean remove(Collection<O> objectsToRemove) {
        return update(objectsToRemove, Collections.emptyList());
    }

    public boolean update(Collection<O> objectsToRemove, Collection<ProvidedKeyTemplate> objectsToAdd) {
        KeySource source = getSource();
        objectsToRemove.forEach(it -> source.remove(getKeyId(it)));
        val newKeys = objectsToAdd.stream()
                .map(source::addAndReturnId)
                .map(this::viewFromId)
                .collect(Collectors.toList());
        return updateCollection(objectsToRemove, newKeys);
    }

    protected abstract KeySource getSource();
    protected abstract String getKeyId(O ofKey);
    protected abstract O viewFromId(String ofKey);
    protected abstract boolean updateCollection(Collection<O> keysToRemove, Collection<O> keysToAdd);
}
