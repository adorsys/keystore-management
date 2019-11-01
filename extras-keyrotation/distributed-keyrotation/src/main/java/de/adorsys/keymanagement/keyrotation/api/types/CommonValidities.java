package de.adorsys.keymanagement.keyrotation.api.types;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Map;

@UtilityClass
public class CommonValidities {

    public static Map<KeyType, Collection<KeyStatus>> DEFAULT_FILTER = ImmutableMap.of(
            KeyType.SECRET, ImmutableSet.of(KeyStatus.VALID),
            KeyType.ENCRYPTING, ImmutableSet.of(KeyStatus.VALID),
            KeyType.SIGNING, ImmutableSet.of(KeyStatus.VALID, KeyStatus.LEGACY)
    );
}
