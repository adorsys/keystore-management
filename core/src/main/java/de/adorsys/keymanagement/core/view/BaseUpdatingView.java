package de.adorsys.keymanagement.core.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.entity.AliasWithMeta;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.view.UpdatingView;
import lombok.val;

import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseUpdatingView<Q, O> implements UpdatingView<Q, O, String> {

    public boolean add(ProvidedKeyTemplate objectToAdd) {
        return add(Collections.singleton(objectToAdd));
    }

    @Override
    public boolean removeById(String objectsToRemove) {
        return removeByIds(Collections.singleton(objectsToRemove));
    }

    @Override
    public boolean removeByIds(Collection<String> objectsToRemove) {
        KeySource source = getSource();
        List<O> allWithAssociated = objectsToRemove.stream()
                .flatMap(it -> source.allAssociatedEntries(it).stream())
                .map(this::requireFromCollection)
                .collect(Collectors.toList());

        objectsToRemove.forEach(source::remove); // Cascades inside KeyStore

        return updateCollection(allWithAssociated, Collections.emptyList());
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
        List<O> allWithAssociated = objectsToRemove.stream()
                .map(this::getKeyId)
                .flatMap(it -> source.allAssociatedEntries(it).stream())
                .map(this::requireFromCollection)
                .collect(Collectors.toList());

        objectsToRemove.forEach(it -> source.remove(getKeyId(it))); // Cascades inside KeyStore

        val newKeys = objectsToAdd.stream()
                .map(source::addAndReturnId)
                .flatMap(it -> source.allAssociatedEntries(it).stream())
                .map(this::fromSource)
                .collect(Collectors.toList());

        return updateCollection(allWithAssociated, newKeys);
    }

    @Override
    public boolean update(Collection<AliasWithMeta> newMetadata) {
        KeySource source = getSource();
        List<O> toReplace = newMetadata.stream().map(it -> fromCollection(it.getAlias())).collect(Collectors.toList());
        newMetadata.forEach(source::updateMetadata);
        List<O> replacement = newMetadata.stream().map(it -> fromSource(it.getAlias())).collect(Collectors.toList());
        return updateCollection(toReplace, replacement);
    }

    protected abstract KeySource getSource();
    protected abstract String getKeyId(O ofKey);
    protected abstract O fromSource(String ofKey);
    protected abstract O fromCollection(String ofKey);
    protected abstract boolean updateCollection(Collection<O> keysToRemove, Collection<O> keysToAdd);

    private O requireFromCollection(String ofKey) {
        O result = fromCollection(ofKey);
        if (null == result) {
            throw new ConcurrentModificationException("Missing " + ofKey + ", probably KeyStore was modified");
        }

        return result;
    }
}
