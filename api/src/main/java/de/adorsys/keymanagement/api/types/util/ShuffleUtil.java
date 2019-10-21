package de.adorsys.keymanagement.api.types.util;

import de.adorsys.keymanagement.api.types.ResultCollection;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ShuffleUtil {

    public <T> ResultCollection<T> shuffleAndSelectN(Collection<T> data, int count) {
        List<T> shuffled = new ArrayList<>(data);
        Collections.shuffle(shuffled);
        return new ResultCollection<>(shuffled.subList(0, count));
    }
}
