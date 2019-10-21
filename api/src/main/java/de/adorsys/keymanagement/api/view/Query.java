package de.adorsys.keymanagement.api.view;

public interface Query<O> {

    boolean matches(O object, Object queryOptions);
}
