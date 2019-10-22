package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;

public interface AliasView extends UpdatingView<KeyAlias> {

    @Override
    QueryResult<KeyAlias> retrieve(Object query);

    @Override
    QueryResult<KeyAlias> retrieve(String query);

    @Override
    ResultCollection<KeyAlias> all();

    KeySource getSource();
}
