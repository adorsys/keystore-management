package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;

public class KeyCollection {

    private final IndexedCollection<KeyMetadata> metadata = new TransactionalIndexedCollection<>(KeyMetadata.class);

    public ResultSet<KeyMetadata> retrieve(Query<KeyMetadata> query) {
        return metadata.retrieve(query);
    }
}
