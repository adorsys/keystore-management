package de.adorsys.keymanagement.core;

import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
// FIXME: Collapse to collection/iterable/streamable
public class KeySet {

    private final List<ProvidedKeyEntry> keyEntries;
    private final List<Provided> keys;
    private final List<ProvidedKeyPair> keyPairs;
}
