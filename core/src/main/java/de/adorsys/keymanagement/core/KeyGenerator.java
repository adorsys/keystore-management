package de.adorsys.keymanagement.core;

import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.Provided;

public class KeyGenerator {

    public Provided generate(Secret template) {
        return null;
    }

    public Provided generate(Signing template) {
        return null;
    }

    public Provided generate(Encrypting template) {
        return null;
    }

    public KeySet generate(KeySetTemplate template) {
        return null;
    }
}
