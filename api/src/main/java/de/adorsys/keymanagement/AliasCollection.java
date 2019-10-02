package de.adorsys.keymanagement;

import java.util.Set;

public interface AliasCollection extends FromKeyStorage {

    Set<String> aliases();
}
