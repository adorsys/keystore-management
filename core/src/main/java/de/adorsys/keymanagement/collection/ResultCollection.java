package de.adorsys.keymanagement.collection;

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
        List<T> shuffled = new ArrayList<>(result);
        Collections.shuffle(shuffled);
        return new ResultCollection<>(shuffled.subList(0, count));
    }

    public ResultCollection<T> shuffle() {
        return pickNrandom(result.size());
    }
}
