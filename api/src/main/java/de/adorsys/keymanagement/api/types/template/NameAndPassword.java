package de.adorsys.keymanagement.api.types.template;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
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

    @Getter
    private final KeyMetadata metadata;

    public NameAndPassword(@NonNull Supplier<char[]> password, KeyMetadata metadata) {
        this.namingStrategy = new DefaultNamingStrategy(null, null);
        this.password = password;
        this.metadata = metadata;
    }

    @Override
    public String generateName() {
        return namingStrategy.generateName();
    }
}
