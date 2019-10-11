package de.adorsys.keymanagement.core;

import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * Heterogeneous collection of keys that can be stored in {@link java.security.KeyStore}.
 */
@Getter
@Builder
@RequiredArgsConstructor
// FIXME: Collapse to collection/iterable/streamable
public class KeySet {

    @Singular
    private final List<ProvidedKeyEntry> keyEntries;

    @Singular
    private final List<ProvidedKey> keys;

    @Singular
    private final List<ProvidedKeyPair> keyPairs;
}
