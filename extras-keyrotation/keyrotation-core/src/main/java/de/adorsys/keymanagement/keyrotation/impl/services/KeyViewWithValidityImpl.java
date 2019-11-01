package de.adorsys.keymanagement.keyrotation.impl.services;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import de.adorsys.keymanagement.api.Juggler;
import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStoreAccess;
import de.adorsys.keymanagement.keyrotation.api.services.KeyViewWithValidity;
import de.adorsys.keymanagement.keyrotation.api.types.KeyRotationConfig;
import de.adorsys.keymanagement.keyrotation.api.types.KeyStatus;
import de.adorsys.keymanagement.keyrotation.api.types.KeyType;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;
import static de.adorsys.keymanagement.keyrotation.api.types.KeyState.*;

@RequiredArgsConstructor
public class KeyViewWithValidityImpl implements KeyViewWithValidity {

    private final KeyRotationConfig config;
    private final Juggler juggler;
    private final KeyStoreAccess access;
    private final Map<KeyType, Collection<KeyStatus>> validityMap;

    @Override
    public ResultCollection<KeyEntry> all() {
        EntryView<Query<KeyEntry>> entries = getEntriesView();
        return applyValidityFilter(validityMap, entries);
    }

    @Override
    public ResultCollection<KeyEntry> encryptionKeys() {
        EntryView<Query<KeyEntry>> entries = getEntriesView();
        return applyValidityFilter(filteredValidityMap(KeyType.ENCRYPTING), entries);
    }

    @Override
    public ResultCollection<KeyEntry> signingKeys() {
        EntryView<Query<KeyEntry>> entries = getEntriesView();
        return applyValidityFilter(filteredValidityMap(KeyType.SIGNING), entries);
    }

    @Override
    public ResultCollection<KeyEntry> secretKeys() {
        EntryView<Query<KeyEntry>> entries = getEntriesView();
        return applyValidityFilter(filteredValidityMap(KeyType.SECRET), entries);
    }

    private Map<KeyType, Collection<KeyStatus>> filteredValidityMap(KeyType forType) {
        return validityMap.entrySet().stream()
                .filter(it -> it.getKey() == forType)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private ResultCollection<KeyEntry> applyValidityFilter(Map<KeyType, Collection<KeyStatus>> validityMap,
                                                           EntryView<Query<KeyEntry>> entries) {
        Set<KeyEntry> result = new HashSet<>();

        for (KeyType type : validityMap.keySet()) {
            result.addAll(
                    entries.retrieve(and(equal(TYPE, type), in(STATUS, validityMap.get(type)))).toCollection()
            );
        }

        return new ResultCollection<>(result);
    }

    private EntryView<Query<KeyEntry>> getEntriesView() {
        return juggler.readKeys().fromKeyStore(
                access.read(),
                id -> config.keyPassword().get()
        ).entries();
    }
}
