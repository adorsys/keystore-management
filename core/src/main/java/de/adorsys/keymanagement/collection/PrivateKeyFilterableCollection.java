package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.resultset.ResultSet;
import lombok.Getter;
import lombok.experimental.Delegate;
import lombok.val;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PrivateKeyFilterableCollection implements Collection<PrivateKey> {

    private final Set<Context> result;

    @Delegate
    private final Set<PrivateKey> keys;

    public PrivateKeyFilterableCollection(ResultSet<QueryableKey> resultSet) {

        this.result = new LinkedHashSet<>();
        try (val res = resultSet.stream()) {
            res.forEach(it -> result.add(new Context(it)));
        }
        this.keys = result.stream().map(Context::getPrivateKey).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private PrivateKeyFilterableCollection(Set<Context> result) {
        this.result = result;
        this.keys = result.stream().map(Context::getPrivateKey).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public PrivateKeyFilterableCollection hasAlias(Predicate<String> aliasFilter) {
        return new PrivateKeyFilterableCollection(
                result.stream()
                        .filter(it -> aliasFilter.test(it.getAlias()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public PrivateKeyFilterableCollection has(Predicate<QueryableKey> queryableKeyFilter) {
        return new PrivateKeyFilterableCollection(
                result.stream()
                        .filter(it -> queryableKeyFilter.test(it.getQueryable()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public PrivateKeyFilterableCollection hasEntry(Predicate<KeyStore.PrivateKeyEntry> entryFilter) {
        return new PrivateKeyFilterableCollection(
                result.stream()
                        .filter(it -> entryFilter.test(it.getKeyEntry()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public PrivateKeyFilterableCollection hasKey(Predicate<PrivateKey> privateKeyFilter) {
        return new PrivateKeyFilterableCollection(
                result.stream()
                        .filter(it -> privateKeyFilter.test(it.getPrivateKey()))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public ResultCollection<PrivateKey> pickNrandom(int count) {
        return ShuffleUtil.shuffleAndSelectN(keys, count);
    }

    public ResultCollection<PrivateKey> shuffle() {
        return pickNrandom(result.size());
    }

    @Getter
    static class Context {
        private final String alias;
        private final QueryableKey queryable;
        private final KeyStore.PrivateKeyEntry keyEntry;
        private final PrivateKey privateKey;

        Context(QueryableKey from) {
            this.alias = from.getAlias();
            this.queryable = from;
            this.keyEntry = (KeyStore.PrivateKeyEntry) from.getKey();
            this.privateKey = keyEntry.getPrivateKey();
        }
    }
}
