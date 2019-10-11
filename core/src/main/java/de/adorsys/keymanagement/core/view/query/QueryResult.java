package de.adorsys.keymanagement.core.view.query;

import com.googlecode.cqengine.resultset.ResultSet;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class QueryResult<T> implements AutoCloseable, Iterable<T> {

    private final ResultSet<T> resultSet;

    public Stream<T> stream() {
        return resultSet.stream().onClose(this::close);
    }

    public int size() {
        return resultSet.size();
    }

    public boolean isEmpty() {
        return resultSet.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return resultSet.iterator();
    }

    @Override
    public void close() {
        resultSet.close();
    }

    public ResultCollection<T> toCollection() {
        try (Stream<T> keys = resultSet.stream()) {
            List<T> collected = keys.collect(Collectors.toList());
            return new ResultCollection<>(new LinkedHashSet<>(collected));
        }
    }
}
