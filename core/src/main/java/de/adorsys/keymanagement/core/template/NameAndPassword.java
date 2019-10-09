package de.adorsys.keymanagement.core.template;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public class NameAndPassword implements KeyTemplate {

    @NonNull
    private final KeyNamingStrategy namingStrategy;
    private final Supplier<char[]> password;

    public NameAndPassword(@NonNull Supplier<char[]> password) {
        this.namingStrategy = new DefaultNamingStrategy(null, null);
        this.password = password;
    }

    @Override
    public String getName() {
        return namingStrategy.getName();
    }
}
