package de.adorsys.keymanagement.keyrotation.api;

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
    private final Duration validity;
    private final Duration legacy;
    private final Map<KeyType, Integer> keysByType;

    @Builder.Default
    private final Map<KeyType, GeneratedKeyTemplate> keyTemplate = ImmutableMap.of(
            KeyType.SECRET, Secret.with().password(keyPassword).build(),
            KeyType.SIGNING, Signing.with().password(keyPassword).build(),
            KeyType.ENCRYPTING, Encrypting.with().password(keyPassword).build()
    );

    @Override
    public Collection<KeyType> getEnabledFor() {
        return keysByType.keySet();
    }

    @Override
    public char[] getKeyPassword() {
        return keyPassword.get();
    }
}
