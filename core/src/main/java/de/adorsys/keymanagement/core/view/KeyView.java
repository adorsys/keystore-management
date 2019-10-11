package de.adorsys.keymanagement.core.view;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import de.adorsys.keymanagement.core.collection.keystore.QueryResult;
import de.adorsys.keymanagement.core.collection.keystore.ResultCollection;
import de.adorsys.keymanagement.core.source.KeySource;
import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.view.entity.Key;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static de.adorsys.keymanagement.core.view.ViewUtil.PROP_NAME;

public class KeyView extends UpdatingView<Key> {

    private static final SQLParser<Key> PARSER = SQLParser.forPojoWithAttributes(
            Key.class,
            createAttributes(Key.class, GETTER_METHODS_ONLY, PROP_NAME)
    );

    @Getter
    private final KeySource source;
    /**
     * Note that keystore aliases are case-insensitive in general case
     */
    private final IndexedCollection<Key> keys = new TransactionalIndexedCollection<>(Key.class);

    public KeyView(KeySource source) {
        this(source, Collections.emptyList());
    }

    @SneakyThrows
    public KeyView(KeySource source, Collection<Index<Key>> indexes) {
        this.source = source;
        keys.addAll(
                source.aliasesFor(ProvidedKey.class)
                        .map(it -> new Key(it, null, source.asKey(it)))
                        .collect(Collectors.toList())
        );
        indexes.forEach(keys::addIndex);
    }

    @Override
    public QueryResult<Key> retrieve(Query<Key> query) {
        return new QueryResult<>(keys.retrieve(query));
    }

    @Override
    public QueryResult<Key> retrieve(String query) {
        return new QueryResult<>(keys.retrieve(PARSER.parse(query).getQuery()));
    }

    @Override
    public ResultCollection<Key> all() {
        return new QueryResult<>(keys.retrieve(QueryFactory.all(Key.class))).toCollection();
    }

    @Override
    protected String getKeyId(Key ofKey) {
        return ofKey.getAlias();
    }

    @Override
    protected Key viewFromId(String ofKey) {
        return new Key(ofKey, null, source.asKey(ofKey)); // FIXME fill metadata
    }

    @Override
    protected boolean updateCollection(Collection<Key> keysToRemove, Collection<Key> keysToAdd) {
        return keys.update(keysToRemove, keysToAdd);
    }
}
