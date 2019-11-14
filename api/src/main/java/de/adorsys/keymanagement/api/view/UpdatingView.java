package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.types.entity.AliasWithMeta;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;

import java.util.Collection;

/**
 * Adds to View interface ability to add, update and remove objects
 * @param <Q> Query
 * @param <O> Object
 * @param <ID> Object identifier
 */
public interface UpdatingView<Q, O, ID> extends View<Q, O> {
    /**
     * Adds new key to key source
     * @param objectToAdd Key template of a key
     * @return true if key is added successfully
     */
    boolean add(ProvidedKeyTemplate objectToAdd);

    /**
     * Removes object from key source by identifier
     * @param objectToRemove Identifier of object to remove
     * @return true if object removed successfully
     */
    boolean removeById(ID objectToRemove);

    /**
     * Removes one or more objects from key source by identifier
     * @param objectsToRemove collection of object identifiers to remove
     * @return true if all objects removed successfully
     */
    boolean removeByIds(Collection<ID> objectsToRemove);

    /**
     * Removes object from key source
     * @param objectToRemove Object to remove
     * @return true if object removed successfully
     */
    boolean remove(O objectToRemove);

    /**
     * Adds one or more objects to key source from key templates
     * @param objectsToAdd collection of key templates
     * @return true if keys are added successfully
     */
    boolean add(Collection<ProvidedKeyTemplate> objectsToAdd);

    /**
     * Removes one or more objects from key source
     * @param objectsToRemove collection of object to remove
     * @return true if all objects removed successfully
     */
    boolean remove(Collection<O> objectsToRemove);

    /**
     * updates content of source objects
     * @param objectsToRemove collection of objects to remove
     * @param objectsToAdd collection of objects to add
     * @return true if all objects are removed and added successfully
     */
    boolean update(Collection<O> objectsToRemove, Collection<ProvidedKeyTemplate> objectsToAdd);

    /**
     * Updates metadata for specified objects
     * @param newMetadata collection of new metadata with aliases
     * @return true if metadata for all objects updated correctly
     */
    boolean update(Collection<AliasWithMeta> newMetadata);
}
