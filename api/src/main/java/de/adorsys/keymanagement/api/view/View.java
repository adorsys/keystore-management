package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.types.ResultCollection;

/**
 * This interface sets general structure of getting objects from some source using query
 * @param <Q> Query
 * @param <O> Object
 */
public interface View<Q, O> {

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<O> retrieve(Q query);

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<O> retrieve(String query);

    /**
     * Guarantees result uniqueness
     * @param query understandable to implementation query.
     * @return query result
     */
    O uniqueResult(Q query);

    /**
     * Guarantees result uniqueness
     * @param query understandable to implementation query as a string
     * @return query result
     */
    O uniqueResult(String query);

    /**
     * Retrieves all elements of target object
     * @return objects as a collection
     */
    ResultCollection<O> all();
}
