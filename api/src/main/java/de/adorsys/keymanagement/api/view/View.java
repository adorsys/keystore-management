package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.types.ResultCollection;

public interface View<O> {

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<O> retrieve(Object query);

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<O> retrieve(String query);

    ResultCollection<O> all();
}
