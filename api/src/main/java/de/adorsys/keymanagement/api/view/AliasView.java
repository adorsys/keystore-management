package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;

public interface AliasView<Q> extends UpdatingView<Q, KeyAlias, String> {

    @Override
    QueryResult<KeyAlias> retrieve(Q query);

    @Override
    QueryResult<KeyAlias> retrieve(String query);

    @Override
    ResultCollection<KeyAlias> all();

    KeySource getSource();
}
