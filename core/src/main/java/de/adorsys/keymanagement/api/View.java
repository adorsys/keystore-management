package de.adorsys.keymanagement.api;

import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.api.types.ResultCollection;

public interface View<T> {

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<T> retrieve(Query<T> query);

    /**
     * Note that client who calls this should close the result.
     */
    QueryResult<T> retrieve(String query);

    ResultCollection<T> all();
}
