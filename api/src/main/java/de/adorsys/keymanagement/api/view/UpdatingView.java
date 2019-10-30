package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.types.entity.AliasWithMeta;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;

import java.util.Collection;

public interface UpdatingView<Q, O, ID> extends View<Q, O> {

    boolean add(ProvidedKeyTemplate objectToAdd);
    boolean removeById(ID objectToRemove);
    boolean removeByIds(Collection<ID> objectsToRemove);
    boolean remove(O objectToRemove);
    boolean add(Collection<ProvidedKeyTemplate> objectsToAdd);
    boolean remove(Collection<O> objectsToRemove);
    boolean update(Collection<O> objectsToRemove, Collection<ProvidedKeyTemplate> objectsToAdd);
    boolean update(Collection<AliasWithMeta> newMetadata);
}
