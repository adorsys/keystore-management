package de.adorsys.keymanagement.template.generated;

import de.adorsys.keymanagement.template.KeyPair;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuperBuilder(builderMethodName = "with", toBuilder = true)
public class Encrypting extends KeyPair {

    public Collection<Encrypting> repeat(int times) {
        return IntStream.range(0, times).boxed().map(it -> this.toBuilder().build()).collect(Collectors.toList());
    }
}
