package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.types.ResultCollection;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * This interface describes actions which could be done with query results
 * @param <T> Type of queried objects
 */
public interface QueryResult<T> extends AutoCloseable, Iterable<T> {
    /**
     * Converts query result to a stream
     * @return stream of T objects
     */
    Stream<T> stream();

    /**
     * Calculate number of objects in query result
     * @return integer number
     */
    int size();

    /**
     * Checks if query result is empty
     * @return true if query result has no objects
     */
    boolean isEmpty();

    /**
     * Iterate through the query resulting objects
     * @return iterator
     */
    @Override
    Iterator<T> iterator();

    /**
     * Closes the stream of query result and releases resources
     */
    @Override
    void close();

    /**
     * Converts query result to collection
     * @return collection of type T objects
     */
    ResultCollection<T> toCollection();
}
