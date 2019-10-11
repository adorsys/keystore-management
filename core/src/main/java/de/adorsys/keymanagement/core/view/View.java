package de.adorsys.keymanagement.core.view;

import com.googlecode.cqengine.query.Query;
import de.adorsys.keymanagement.core.view.query.QueryResult;
import de.adorsys.keymanagement.core.view.query.ResultCollection;

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
