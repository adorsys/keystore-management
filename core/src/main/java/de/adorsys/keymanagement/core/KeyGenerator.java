package de.adorsys.keymanagement.core;

import de.adorsys.keymanagement.api.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.SecretKeyGenerator;
import de.adorsys.keymanagement.api.SigningKeyGenerator;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;

import java.util.ArrayList;
import java.util.List;

public class KeyGenerator {

    private final EncryptingKeyGenerator encryptingKeyGenerator;
    private final SecretKeyGenerator secretKeyGenerator;
    private final SigningKeyGenerator signingKeyGenerator;

    public KeyGenerator(EncryptingKeyGenerator encryptingKeyGenerator, SecretKeyGenerator secretKeyGenerator,
                        SigningKeyGenerator signingKeyGenerator) {
        this.encryptingKeyGenerator = encryptingKeyGenerator;
        this.secretKeyGenerator = secretKeyGenerator;
        this.signingKeyGenerator = signingKeyGenerator;
    }

    public Provided generate(Secret template) {
        return secretKeyGenerator.generate(template);
    }

    public ProvidedKeyPair generate(Signing template) {
        return signingKeyGenerator.generate(template);
    }

    public ProvidedKeyPair generate(Encrypting template) {
        return encryptingKeyGenerator.generate(template);
    }

    public KeySet generate(KeySetTemplate template) {
        List<Provided> provideds = new ArrayList<>(template.providedKeys());
        List<ProvidedKeyPair> providedPairs = new ArrayList<>(template.providedPairs());
        List<ProvidedKeyEntry> entries = new ArrayList<>(template.providedKeyEntries());

        template.generatedSecretKeys().forEach(it -> provideds.add(generate(it)));
        template.generatedSigningKeys().forEach(it -> providedPairs.add(generate(it)));
        template.generatedEncryptionKeys().forEach(it -> providedPairs.add(generate(it)));

        // FIXME - validate key alias/name uniqueness
        return new KeySet(entries, provideds, providedPairs);
    }
}
