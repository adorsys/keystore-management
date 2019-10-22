package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;

public interface EntryView<Q> extends UpdatingView<Q, KeyEntry> {

    @Override
    QueryResult<KeyEntry> retrieve(Q query);

    @Override
    QueryResult<KeyEntry> retrieve(String query);

    @Override
    ResultCollection<KeyEntry> all();

    QueryResult<KeyEntry> secretKeys();

    QueryResult<KeyEntry> privateKeys();

    QueryResult<KeyEntry> trustedCerts();

    KeySource getSource();
}
