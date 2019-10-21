package de.adorsys.keymanagement.core.types;

import de.adorsys.keymanagement.core.view.query.ShuffleUtil;
import lombok.experimental.Delegate;

import java.util.*;

/**
 * This class is safe to use without try-with-resources as it has all keys loaded.
 * @param <T>
 */
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
