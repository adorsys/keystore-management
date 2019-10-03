package de.adorsys.keymanagement.template.generated;

import de.adorsys.keymanagement.template.NamedWithPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@SuperBuilder(builderMethodName = "with", toBuilder = true)
public class Secret extends NamedWithPassword {

    @Builder.Default
    private final String algo = "AES";

    @Builder.Default
    private final int size = 256;

    public Collection<Secret> repeat(int times) {
        return IntStream.range(0, times).boxed().map(it -> this).collect(Collectors.toList());
    }
}
