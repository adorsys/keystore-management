package de.adorsys.keymanagement.api;

import java.util.Collection;

public interface ModifiableView<R, A> {

    boolean update(Collection<R> objectsToRemove, Collection<A> objectsToAdd);
}
