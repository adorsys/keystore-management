package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@SuppressWarnings("checkstyle:HideUtilityClassConstructor") // Lombok-generates private ctor
public class BatchValidator {

    public static List<String> filterNull(List<KeyValue> input) {
        return input.stream()
                .filter(KeyValue::isNull)
                .map(KeyValue::getKey)
                .collect(Collectors.toList());
    }
}
