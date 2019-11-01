package de.adorsys.keymanagement.keyrotation.api.types;

import com.google.common.collect.ImmutableMap;
import de.adorsys.keymanagement.api.types.template.GeneratedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.generated.Encrypting;
import de.adorsys.keymanagement.api.types.template.generated.Secret;
import de.adorsys.keymanagement.api.types.template.generated.Signing;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@Builder
public class RotationConfig implements KeyRotationConfig {

    private final Supplier<char[]> keyPassword;
    private final Supplier<char[]> keyStorePassword;
    private final Duration validity;
    private final Duration legacy;
    private final Map<KeyType, Integer> countValidByType;

    @Builder.Default
    private final Map<KeyType, GeneratedKeyTemplate> keyTemplate = ImmutableMap.of(
            KeyType.SECRET, Secret.with().build(),
            KeyType.SIGNING, Signing.with().build(),
            KeyType.ENCRYPTING, Encrypting.with().build()
    );

    @Override
    public Collection<KeyType> getEnabledFor() {
        return countValidByType.keySet();
    }

    @Override
    public Supplier<char[]> keyPassword() {
        return keyPassword;
    }

    @Override
    public Supplier<char[]> keyStorePassword() {
        return keyStorePassword;
    }
}
