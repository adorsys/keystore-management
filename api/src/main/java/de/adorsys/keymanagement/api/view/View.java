package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.types.ResultCollection;

public interface View<Q, O> {

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<O> retrieve(Q query);

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<O> retrieve(String query);

    O uniqueResult(Q query);
    O uniqueResult(String query);
    ResultCollection<O> all();
}
