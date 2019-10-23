package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;

public interface EncryptingKeyGenerator {

    KeyPairEntry generate(Encrypting fromTemplate);
}
