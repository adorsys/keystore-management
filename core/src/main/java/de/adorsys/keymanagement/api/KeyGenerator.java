package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.generator.KeySetTemplate;
import de.adorsys.keymanagement.core.source.KeySet;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;

public interface KeyGenerator {

    ProvidedKey generate(Secret template);
    ProvidedKeyPair generate(Signing template);
    ProvidedKeyPair generate(Encrypting template);
    KeySet generate(KeySetTemplate template);
}
