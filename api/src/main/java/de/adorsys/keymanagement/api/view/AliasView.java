package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;

/**
 * This view uses key aliases as source objects
 * @param <Q> Query
 */
public interface AliasView<Q> extends UpdatingView<Q, KeyAlias, String> {

    /**
     * Note that client who calls this should close the result.
     * @param query understandable to implementation query.
     * @return key aliases which match queried criteria
     */
    @Override
    QueryResult<KeyAlias> retrieve(Q query);

    /**
     * Note that client who calls this should close the result.
     * @param query understandable to implementation query.
     * @return key aliases which match queried criteria
     */
    @Override
    QueryResult<KeyAlias> retrieve(String query);

    /**
     * Retrieves all available key aliases
     * @return collection of aliases
     */
    @Override
    ResultCollection<KeyAlias> all();

    /**
     * Access to key source
     * @return key source object
     */
    KeySource getSource();
}
