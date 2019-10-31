package de.adorsys.keymanagement.keyrotation.impl.services;

import de.adorsys.keymanagement.keyrotation.api.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.KeyType;
import de.adorsys.keymanagement.keyrotation.api.KeyView;
import de.adorsys.keymanagement.keyrotation.api.KeyViewWithValidity;

import java.util.Collection;
import java.util.Map;

public class KeyViewImpl implements KeyView {

    @Override
    public KeyViewWithValidity withValidity(Map<KeyType, Collection<KeyStatus>> validityMap) {
        return null;
    }
}
