package de.adorsys.keymanagement.keyrotation.impl.services;

import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyView;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyViewWithValidity;

import javax.inject.Inject;
import java.security.Key;
import java.util.Collection;
import java.util.Map;

public class KeyViewImpl implements KeyView {

    @Inject
    public KeyViewImpl() {
    }

    @Override
    public KeyViewWithValidity withValidity(Map<KeyType, Collection<KeyStatus>> validityMap) {
        return null;
    }

    @Override
    public Key keyById(String id) {
        return null;
    }
}
