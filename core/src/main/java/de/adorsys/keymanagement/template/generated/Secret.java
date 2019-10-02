package de.adorsys.keymanagement.template.generated;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(builderMethodName = "with")
public class Secret extends GeneratedKeyTemplate {

    @Builder.Default
    private final String algo = "AES";

    @Builder.Default
    private final int size = 256;
}
