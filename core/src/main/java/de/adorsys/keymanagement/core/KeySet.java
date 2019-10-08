package de.adorsys.keymanagement.core;

import de.adorsys.keymanagement.core.template.provided.Provided;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class KeySet {

    private final Set<Provided> keys;
}
