package de.adorsys.keymanagement.api;

import com.googlecode.cqengine.resultset.ResultSet;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.view.QueryResult;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CqeQueryResult<T> implements QueryResult<T> {

    private final ResultSet<T> resultSet;

    @Override
    public Stream<T> stream() {
        return resultSet.stream().onClose(this::close);
    }

    @Override
    public int size() {
        return resultSet.size();
    }

    @Override
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

    @Override
    public ResultCollection<T> toCollection() {
        try (Stream<T> keys = resultSet.stream()) {
            List<T> collected = keys.collect(Collectors.toList());
            return new ResultCollection<>(new LinkedHashSet<>(collected));
        }
    }
}
