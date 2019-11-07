package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import java.util.ArrayList;
import java.util.List;

public final class ListOfKeyValueBuilder {
    private final List<KeyValue> list = new ArrayList<>();

    private ListOfKeyValueBuilder() {
    }

    public ListOfKeyValueBuilder add(String key, Object value) {
        list.add(new KeyValue(key, value));
        return this;
    }

    public static ListOfKeyValueBuilder newBuilder() {
        return new ListOfKeyValueBuilder();
    }

    public List<KeyValue> build() {
        return list;
    }
}
