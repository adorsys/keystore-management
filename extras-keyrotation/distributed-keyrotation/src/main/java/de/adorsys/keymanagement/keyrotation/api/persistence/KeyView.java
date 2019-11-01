package de.adorsys.keymanagement.keyrotation.api.persistence;

import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;

import java.security.Key;
import java.util.Collection;
import java.util.Map;

public interface KeyView {

    KeyViewWithValidity withValidity(Map<KeyType, Collection<KeyStatus>> validityMap);
    Key keyById(String id);
}
