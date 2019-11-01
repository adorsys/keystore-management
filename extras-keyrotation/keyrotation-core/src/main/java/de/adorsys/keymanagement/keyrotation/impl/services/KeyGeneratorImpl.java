package de.adorsys.keymanagement.keyrotation.impl.services;

import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.api.types.template.NameAndPassword;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.keyrotation.api.services.KeyGenerator;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.api.types.KeyState;
import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;

import javax.inject.Inject;
import java.time.Instant;

public class KeyGeneratorImpl implements KeyGenerator {

    private final Juggler juggler;
    private final KeyRotationConfig config;

    @Inject
    public KeyGeneratorImpl(Juggler juggler, KeyRotationConfig config) {
        this.juggler = juggler;
        this.config = config;
    }

    @Override
    public ProvidedKeyTemplate generateValidKey(Instant now, KeyType forType) {
        if (forType == KeyType.SECRET) {
            return juggler.generateKeys().secret(
                    ((Secret) config.getKeyTemplate().get(forType))
                            .toBuilder()
                            .keyTemplate(new NameAndPassword(config.getKeyPassword()))
                            .metadata(stateForValidKey(now, forType))
                            .build()
            );
        }

        if (forType == KeyType.SIGNING) {
            return juggler.generateKeys().signing(
                    ((Signing) config.getKeyTemplate().get(forType))
                            .toBuilder()
                            .keyTemplate(new NameAndPassword(config.getKeyPassword()))
                            .metadata(stateForValidKey(now, forType))
                            .build()
            );
        }

        if (forType == KeyType.ENCRYPTING) {
            return juggler.generateKeys().encrypting(
                    ((Encrypting) config.getKeyTemplate().get(forType))
                            .toBuilder()
                            .keyTemplate(new NameAndPassword(config.getKeyPassword()))
                            .metadata(stateForValidKey(now, forType))
                            .build()
            );
        }

        throw new IllegalArgumentException("Unknown generator for key type: " + forType.name());
    }

    private KeyState stateForValidKey(Instant now, KeyType type) {
        return KeyState.builder()
                .status(KeyStatus.VALID)
                .type(type)
                .createdAt(now)
                .notAfter(now.plus(config.getValidity()))
                .expireAt(now.plus(config.getLegacy()))
                .build();
    }
}
