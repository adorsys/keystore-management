package de.adorsys.keymanagement.template.generated;

import de.adorsys.keymanagement.template.NamedWithPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(builderMethodName = "with", toBuilder = true)
public class Secret extends NamedWithPassword {

    @Builder.Default
    private final String algo = "AES";

    @Builder.Default
    private final int size = 256;
}
