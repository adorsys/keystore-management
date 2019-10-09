package de.adorsys.keymanagement.core.collection.keystore;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Supplier;

@Getter
@Builder
public class QueryableKeyWithPassword {

    @NonNull
    private final QueryableKey key;

    @NonNull
    private final Supplier<char[]> password;
}
