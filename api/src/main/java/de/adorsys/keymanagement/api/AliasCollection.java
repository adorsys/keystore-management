package de.adorsys.keymanagement.api;

import java.util.Set;

public interface AliasCollection extends FromKeyStorage {

    Set<String> aliases();
}
