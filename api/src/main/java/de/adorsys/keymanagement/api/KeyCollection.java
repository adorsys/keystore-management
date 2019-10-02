package de.adorsys.keymanagement.api;

import java.util.Set;

public interface KeyCollection extends FromKeyStorage {

    Set<Key> keys();
}
