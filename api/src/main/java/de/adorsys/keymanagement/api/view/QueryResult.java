package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.types.ResultCollection;

import java.util.Iterator;
import java.util.stream.Stream;

public interface QueryResult<T> extends AutoCloseable, Iterable<T> {

    Stream<T> stream();

    int size();

    boolean isEmpty();

    @Override
    Iterator<T> iterator();

    @Override
    void close();

    ResultCollection<T> toCollection();
}
