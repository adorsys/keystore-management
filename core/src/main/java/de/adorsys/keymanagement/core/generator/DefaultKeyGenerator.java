package de.adorsys.keymanagement.core.generator;

import de.adorsys.keymanagement.api.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.KeyGenerator;
import de.adorsys.keymanagement.api.SecretKeyGenerator;
import de.adorsys.keymanagement.api.SigningKeyGenerator;
import de.adorsys.keymanagement.core.source.KeySet;
import de.adorsys.keymanagement.core.template.generated.Encrypting;
import de.adorsys.keymanagement.core.template.generated.Secret;
import de.adorsys.keymanagement.core.template.generated.Signing;
import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;

import java.util.ArrayList;
import java.util.List;

public class DefaultKeyGenerator implements KeyGenerator {

    private final EncryptingKeyGenerator encryptingKeyGenerator;
    private final SecretKeyGenerator secretKeyGenerator;
    private final SigningKeyGenerator signingKeyGenerator;

    public DefaultKeyGenerator(EncryptingKeyGenerator encryptingKeyGenerator, SecretKeyGenerator secretKeyGenerator,
                               SigningKeyGenerator signingKeyGenerator) {
        this.encryptingKeyGenerator = encryptingKeyGenerator;
        this.secretKeyGenerator = secretKeyGenerator;
        this.signingKeyGenerator = signingKeyGenerator;
    }

    @Override
    public ProvidedKey generate(Secret template) {
        return secretKeyGenerator.generate(template);
    }

    @Override
    public ProvidedKeyPair generate(Signing template) {
        return signingKeyGenerator.generate(template);
    }

    @Override
    public ProvidedKeyPair generate(Encrypting template) {
        return encryptingKeyGenerator.generate(template);
    }

    @Override
    public KeySet generate(KeySetTemplate template) {
        List<ProvidedKey> providedKeys = new ArrayList<>(template.providedKeys());
        List<ProvidedKeyPair> providedPairs = new ArrayList<>(template.providedPairs());
        List<ProvidedKeyEntry> entries = new ArrayList<>(template.providedKeyEntries());

        template.generatedSecretKeys().forEach(it -> providedKeys.add(generate(it)));
        template.generatedSigningKeys().forEach(it -> providedPairs.add(generate(it)));
        template.generatedEncryptionKeys().forEach(it -> providedPairs.add(generate(it)));

        // FIXME - validate key alias/name uniqueness
        return new KeySet(entries, providedKeys, providedPairs);
    }
}
