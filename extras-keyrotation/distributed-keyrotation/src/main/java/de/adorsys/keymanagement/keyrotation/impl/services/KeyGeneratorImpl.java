package de.adorsys.keymanagement.keyrotation.impl.services;

import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.keyrotation.api.KeyGenerator;
import de.adorsys.keymanagement.keyrotation.api.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.api.KeyType;

import javax.inject.Inject;

public class KeyGeneratorImpl implements KeyGenerator {

    private final Juggler juggler;
    private final KeyRotationConfig config;

    @Inject
    public KeyGeneratorImpl(Juggler juggler, KeyRotationConfig config) {
        this.juggler = juggler;
        this.config = config;
    }

    @Override
    public ProvidedKeyTemplate generateValidKey(KeyType forType) {
        if (forType == KeyType.SECRET) {
            return juggler.generateKeys().secret((Secret) config.getKeyTemplate().get(forType));
        }

        if (forType == KeyType.SIGNING) {
            return juggler.generateKeys().signing((Signing) config.getKeyTemplate().get(forType));
        }

        if (forType == KeyType.ENCRYPTING) {
            return juggler.generateKeys().encrypting((Encrypting) config.getKeyTemplate().get(forType));
        }

        throw new IllegalArgumentException("Unknown generator for key type: " + forType.name());
    }
}
