package de.adorsys.keymanagement.core.collection.keystore;

import com.googlecode.cqengine.resultset.ResultSet;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterableCollection<E, K> implements Collection<K> {

    private final Set<Context<E, K>> result;

    @Delegate
    private final Set<K> keys;

    public FilterableCollection(ResultSet<QueryableKey> resultSet, Function<E, K> getKey) {
        this.result = new LinkedHashSet<>();
        // not necessary to close, because ResultSet<QueryableKey> resultSet should itself be in try-with-resources
        resultSet.stream().forEach(it -> result.add(new Context<>(it, getKey)));
        this.keys = result.stream().map(Context::getKey).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private FilterableCollection(Set<Context<E, K>> result) {
        this.result = result;
        this.keys = result.stream().map(Context::getKey).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public FilterableCollection<E, K> hasAlias(Predicate<String> aliasFilter) {
        return new FilterableCollection<>(
                result.stream()
                        .filter(it -> aliasFilter.test(it.getAlias()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public FilterableCollection<E, K> has(Predicate<QueryableKey> queryableKeyFilter) {
        return new FilterableCollection<>(
                result.stream()
                        .filter(it -> queryableKeyFilter.test(it.getQueryable()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public FilterableCollection<E, K> hasEntry(Predicate<E> entryFilter) {
        return new FilterableCollection<>(
                result.stream()
                        .filter(it -> entryFilter.test(it.getKeyEntry()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public FilterableCollection<E, K> hasKey(Predicate<K> privateKeyFilter) {
        return new FilterableCollection<>(
                result.stream()
                        .filter(it -> privateKeyFilter.test(it.getKey()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public ResultCollection<K> pickNrandom(int count) {
        return ShuffleUtil.shuffleAndSelectN(keys, count);
    }

    public ResultCollection<K> shuffle() {
        return pickNrandom(result.size());
    }

    public Collection<QueryableKey> asQueryable() {
        return result.stream().map(Context::getQueryable).collect(Collectors.toList());
    }

    @Getter
    static class Context<E, K> {
        private final String alias;
        private final QueryableKey queryable;
        private final E keyEntry;
        private final K key;

        Context(QueryableKey from, Function<E, K> getKey) {
            this.alias = from.getAlias();
            this.queryable = from;
            this.keyEntry = (E) from.getKey();
            this.key = getKey.apply(this.keyEntry);
        }
    }
}
