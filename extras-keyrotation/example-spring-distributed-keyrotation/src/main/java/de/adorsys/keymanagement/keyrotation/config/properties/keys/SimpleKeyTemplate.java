package de.adorsys.keymanagement.keyrotation.config.properties.keys;

import de.adorsys.keymanagement.api.types.template.GeneratedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;
import de.adorsys.keymanagement.keyrotation.api.types.RotationConfig;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Key template that allows you to set i.e. key size for encrypting key.
 * I.e.
 * key-templates:
 *   secret:
 *     algo: AES
 *     size: 128
 * maps to secret key with AES and size 128
 */
@Data
public class SimpleKeyTemplate {

    private SimpleKeyDefinition secret;
    private SimpleKeyDefinition encrypting;
    private SimpleKeyDefinition signing;

    private Map<KeyType, GeneratedKeyTemplate> container = new ConcurrentHashMap<>(
            RotationConfig.builder().build().getKeyTemplate()
    );

    public SimpleKeyDefinition getSecret() {
        return secret;
    }

    public SimpleKeyDefinition getEncrypting() {
        return encrypting;
    }

    public SimpleKeyDefinition getSigning() {
        return signing;
    }

    public void setSecret(SimpleKeyDefinition definition) {
        container.put(KeyType.SECRET, Secret.with().algo(definition.getAlgo()).keySize(definition.getSize()).build());
    }

    public void setEncrypting(SimpleKeyDefinition definition) {
        container.put(
                KeyType.ENCRYPTING,
                Encrypting.with()
                        .algo(definition.getAlgo())
                        .keySize(definition.getSize())
                        .sigAlgo(definition.getSigAlgo())
                        .build()
        );
    }

    public void setSigning(SimpleKeyDefinition definition) {
        container.put(
                KeyType.SIGNING,
                Signing.with()
                        .algo(definition.getAlgo())
                        .keySize(definition.getSize())
                        .sigAlgo(definition.getSigAlgo())
                        .build()
        );
    }
}
