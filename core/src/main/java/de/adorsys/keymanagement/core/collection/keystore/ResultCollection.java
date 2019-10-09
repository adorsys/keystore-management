package de.adorsys.keymanagement.core.collection.keystore;

import lombok.experimental.Delegate;

import java.util.*;

public class ResultCollection<T> implements Collection<T> {

    @Delegate
    protected final Set<T> result;

    public ResultCollection(Set<T> result) {
        this.result = result;
    }

    public ResultCollection(List<T> result) {
        this.result = new LinkedHashSet<>(result);
    }

    public ResultCollection<T> pickNrandom(int count) {
        return ShuffleUtil.shuffleAndSelectN(result, count);
    }

    public ResultCollection<T> shuffle() {
        return pickNrandom(result.size());
    }
}
