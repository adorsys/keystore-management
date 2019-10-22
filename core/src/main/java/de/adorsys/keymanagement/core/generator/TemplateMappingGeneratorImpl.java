package de.adorsys.keymanagement.core.generator;

import de.adorsys.keymanagement.api.generator.EncryptingKeyGenerator;
import de.adorsys.keymanagement.api.generator.KeyGenerator;
import de.adorsys.keymanagement.api.generator.SecretKeyGenerator;
import de.adorsys.keymanagement.api.generator.SigningKeyGenerator;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKey;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyPair;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TemplateMappingGeneratorImpl implements KeyGenerator {

    private final EncryptingKeyGenerator encryptingKeyGenerator;
    private final SecretKeyGenerator secretKeyGenerator;
    private final SigningKeyGenerator signingKeyGenerator;

    @Inject
    public TemplateMappingGeneratorImpl(EncryptingKeyGenerator encryptingKeyGenerator, SecretKeyGenerator secretKeyGenerator,
                                        SigningKeyGenerator signingKeyGenerator) {
        this.encryptingKeyGenerator = encryptingKeyGenerator;
        this.secretKeyGenerator = secretKeyGenerator;
        this.signingKeyGenerator = signingKeyGenerator;
    }

    @Override
    public ProvidedKey secret(Secret template) {
        return secretKeyGenerator.generate(template);
    }

    @Override
    public ProvidedKeyPair signing(Signing template) {
        return signingKeyGenerator.generate(template);
    }

    @Override
    public ProvidedKeyPair encrypting(Encrypting template) {
        return encryptingKeyGenerator.generate(template);
    }

    @Override
    public KeySet fromTemplate(KeySetTemplate template) {
        List<ProvidedKey> providedKeys = new ArrayList<>(template.providedKeys());
        List<ProvidedKeyPair> providedPairs = new ArrayList<>(template.providedPairs());
        List<ProvidedKeyEntry> entries = new ArrayList<>(template.providedKeyEntries());

        template.generatedSecretKeys().forEach(it -> providedKeys.add(secret(it)));
        template.generatedSigningKeys().forEach(it -> providedPairs.add(signing(it)));
        template.generatedEncryptionKeys().forEach(it -> providedPairs.add(encrypting(it)));

        // FIXME - validate key alias/name uniqueness
        return new KeySet(entries, providedKeys, providedPairs);
    }
}
