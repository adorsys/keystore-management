package de.adorsys.keymanagement.keyrotation.impl.services;

import com.googlecode.cqengine.query.QueryFactory;
import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStoreAccess;
import de.adorsys.keymanagement.keyrotation.api.services.KeyView;
import de.adorsys.keymanagement.keyrotation.api.services.KeyViewWithValidity;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;

import javax.inject.Inject;
import java.security.KeyStore;
import java.util.Collection;
import java.util.Map;

import static de.adorsys.keymanagement.core.view.EntryViewImpl.A_ID;

public class KeyViewImpl implements KeyView {

    private final KeyRotationConfig config;
    private final KeyStoreAccess access;
    private final Juggler juggler;

    @Inject
    public KeyViewImpl(KeyRotationConfig config, KeyStoreAccess access, Juggler juggler) {
        this.config = config;
        this.access = access;
        this.juggler = juggler;
    }

    @Override
    public KeyViewWithValidity withValidity(Map<KeyType, Collection<KeyStatus>> validityMap) {
        return new KeyViewWithValidityImpl(config, juggler, access, validityMap);
    }

    @Override
    public KeyEntry keyById(String id) {
        return juggler.readKeys()
                .fromKeyStore(keyStore(), keyId -> config.keyPassword().get())
                .entries()
                .uniqueResult(QueryFactory.equal(A_ID, id));
    }

    @Override
    public KeyStore keyStore() {
        return access.read();
    }
}
