package de.adorsys.keymanagement.keyrotation.api.types;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Map;

@UtilityClass
@SuppressWarnings("checkstyle:HideUtilityClassConstructor") // Lombok-generates private ctor
public class CommonValidities {

    public static final Map<KeyType, Collection<KeyStatus>> DEFAULT_VALIDITY = ImmutableMap.of(
            KeyType.SECRET, ImmutableSet.of(KeyStatus.VALID),
            KeyType.ENCRYPTING, ImmutableSet.of(KeyStatus.VALID),
            KeyType.SIGNING, ImmutableSet.of(KeyStatus.VALID, KeyStatus.LEGACY)
    );
}
