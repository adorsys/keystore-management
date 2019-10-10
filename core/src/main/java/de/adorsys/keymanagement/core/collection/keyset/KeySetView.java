package de.adorsys.keymanagement.core.collection.keyset;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import de.adorsys.keymanagement.core.KeySet;
import de.adorsys.keymanagement.core.collection.keystore.QueryResult;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static de.adorsys.keymanagement.core.collection.keyset.QueryableProvided.*;

public class KeySetView {

    private static final SQLParser<QueryableProvided> PARSER = SQLParser.forPojoWithAttributes(
            QueryableProvided.class,
            createAttributes(QueryableProvided.class, GETTER_METHODS_ONLY)
    );

    @Getter
    private final KeySet source;

    private final IndexedCollection<QueryableProvided> keys = new TransactionalIndexedCollection<>(QueryableProvided.class);

    public KeySetView(KeySet source) {
        this(source, Collections.emptyList());
    }

    public KeySetView(KeySet source, Collection<Index<QueryableProvided>> indexes) {
        this.source = source;
        keys.addAll(readKeys(source));
        keys.addIndex(RadixTreeIndex.onAttribute(ID));
        keys.addIndex(HashIndex.onAttribute(IS_PRIVATE));
        keys.addIndex(HashIndex.onAttribute(IS_SECRET));
        indexes.forEach(keys::addIndex);
    }

    /**
     * Note that client who calls this should close the result.
     */
    public QueryResult<QueryableProvided> retrieve(Query<QueryableProvided> query) {
        return new QueryResult<>(keys.retrieve(query));
    }

    /**
     * Note that client who calls this should close the result.
     */
    public QueryResult<QueryableProvided> retrieve(String query) {
        return new QueryResult<>(PARSER.retrieve(keys, query));
    }

    @SneakyThrows
    private List<QueryableProvided> readKeys(KeySet source) {
        val result = new ArrayList<QueryableProvided>();
        source.getKeys().forEach(it -> result.add(QueryableProvided.builder().key(it).build()));
        source.getKeyPairs().forEach(it -> result.add(QueryableProvided.builder().pair(it).build()));
        source.getKeyEntries().forEach(it -> result.add(QueryableProvided.builder().entry(it).build()));
        return result;
    }
}
