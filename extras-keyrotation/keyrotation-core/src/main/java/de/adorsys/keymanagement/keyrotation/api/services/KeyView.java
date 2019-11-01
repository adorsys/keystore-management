package de.adorsys.keymanagement.keyrotation.api.services;

import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;

import java.security.KeyStore;
import java.util.Collection;
import java.util.Map;

public interface KeyView {

    KeyViewWithValidity withValidity(Map<KeyType, Collection<KeyStatus>> validityMap);
    KeyEntry keyById(String id);
    KeyStore keyStore();
}
