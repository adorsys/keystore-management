package de.adorsys.keymanagement.api.view;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;

/**
 * This view uses key entries as source objects
 * @param <Q> query
 */
public interface EntryView<Q> extends UpdatingView<Q, KeyEntry, String> {
    /**
     * Note that client who calls this should close the result.
     * @param query understandable to implementation query.
     * @return key entries which match queried criteria
     */
    @Override
    QueryResult<KeyEntry> retrieve(Q query);

    /**
     * Note that client who calls this should close the result.
     * @param query understandable to implementation query.
     * @return key entries which match queried criteria
     */
    @Override
    QueryResult<KeyEntry> retrieve(String query);

    /**
     * Retrieves all available key entries
     * @return collection of key entries
     */
    @Override
    ResultCollection<KeyEntry> all();

    /**
     * Filters only secret keys
     * @return secret key entries
     */
    QueryResult<KeyEntry> secretKeys();

    /**
     * Filters only private keys
     * @return private key entries
     */
    QueryResult<KeyEntry> privateKeys();

    /**
     * Filters only certificates
     * @return certificate entries
     */
    QueryResult<KeyEntry> trustedCerts();

    /**
     * Access to key source
     * @return key source object
     */
    KeySource getSource();
}
