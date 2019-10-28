package de.adorsys.keymanagement.api.generator;

import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Pbe;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyPair;

public interface KeyGenerator {

    // Secret will be stored as is (without salt and key derivation)
    ProvidedKey secretRaw(Pbe template);
    // Key derivation procedure will be applied
    ProvidedKey secret(Pbe template);
    ProvidedKey secret(Secret template);
    ProvidedKeyPair signing(Signing template);
    ProvidedKeyPair encrypting(Encrypting template);
    KeySet fromTemplate(KeySetTemplate template);
}
