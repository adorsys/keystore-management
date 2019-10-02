package de.adorsys.keymanagement;

import java.util.Set;

public interface KeyCollection extends FromKeyStorage {

    Set<Key> keys();
}
