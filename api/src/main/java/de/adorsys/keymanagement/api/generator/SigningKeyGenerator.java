package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.template.generated.Signing;

public interface SigningKeyGenerator {

    KeyPairEntry generate(Signing fromTemplate);
}
