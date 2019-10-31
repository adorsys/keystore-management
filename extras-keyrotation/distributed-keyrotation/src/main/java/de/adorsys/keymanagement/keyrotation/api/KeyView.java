package de.adorsys.keymanagement.keyrotation.api;

import java.util.Collection;
import java.util.Map;

public interface KeyView {

    KeyViewWithValidity withValidity(Map<KeyType, Collection<KeyStatus>> validityMap);
}
