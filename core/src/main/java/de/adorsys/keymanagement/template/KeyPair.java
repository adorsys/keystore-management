package de.adorsys.keymanagement.template;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
// FIXME Provider-pluggable, dagger configured
@SuperBuilder
public class KeyPair extends NamedWithPassword {

    @Builder.Default
    private final String algo = "RSA";

    @Builder.Default
    private final int size = 2048;

    @Builder.Default
    private final String sigAlgo = "SHA256withRSA";
}
