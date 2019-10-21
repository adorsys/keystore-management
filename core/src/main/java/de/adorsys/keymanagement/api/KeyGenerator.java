package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.types.KeySetTemplate;
import de.adorsys.keymanagement.core.types.source.KeySet;
import de.adorsys.keymanagement.core.types.template.generated.Encrypting;
import de.adorsys.keymanagement.core.types.template.generated.Secret;
import de.adorsys.keymanagement.core.types.template.generated.Signing;
import de.adorsys.keymanagement.core.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.types.template.provided.ProvidedKeyPair;

public interface KeyGenerator {

    ProvidedKey secret(Secret template);
    ProvidedKeyPair signing(Signing template);
    ProvidedKeyPair encrypting(Encrypting template);
    KeySet fromTemplate(KeySetTemplate template);
}
