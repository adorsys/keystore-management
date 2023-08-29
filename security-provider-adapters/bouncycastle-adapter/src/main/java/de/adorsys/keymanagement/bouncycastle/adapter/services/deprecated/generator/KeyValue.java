package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import lombok.Value;

@Value
public class KeyValue {
    private String key;
    private Object value;

    public boolean isNull() {
        return null == value;
    }
}
