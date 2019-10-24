package de.adorsys.keymanagement.core.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.view.UpdatingView;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseUpdatingView<Q, O> implements UpdatingView<Q, O> {

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
        List<O> toRemove = objectsToRemove.stream()
                .map(this::getKeyId)
                .flatMap(it -> source.allAssociatedEntries(it).stream())
                .map(this::getAndValidateViewFromId)
                .collect(Collectors.toList());

        objectsToRemove.forEach(it -> source.remove(getKeyId(it)));

        val newKeys = objectsToAdd.stream()
                .map(source::addAndReturnId)
                .flatMap(it -> source.allAssociatedEntries(it).stream())
                .map(this::newViewFromId)
                .collect(Collectors.toList());

        return updateCollection(toRemove, newKeys);
    }

    protected abstract KeySource getSource();
    protected abstract String getKeyId(O ofKey);
    protected abstract O newViewFromId(String ofKey);
    protected abstract O getViewFromId(String ofKey);
    protected abstract boolean updateCollection(Collection<O> keysToRemove, Collection<O> keysToAdd);

    private O getAndValidateViewFromId(String ofKey) {
        O result = getViewFromId(ofKey);
        if (null == result) {
            throw new ConcurrentModificationException("Missing " + ofKey + ", probably KeyStore was modified");
        }

        return result;
    }
}
