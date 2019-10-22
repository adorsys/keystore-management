package de.adorsys.keymanagement.core.view;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import de.adorsys.keymanagement.api.CqeQueryResult;
import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyPair;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyPair;
import de.adorsys.keymanagement.api.view.QueryResult;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static de.adorsys.keymanagement.core.view.ViewUtil.SNAKE_CASE;

public class PairView extends BaseUpdatingView<Query<KeyPair>, KeyPair> {

    private static final SQLParser<KeyPair> PARSER = SQLParser.forPojoWithAttributes(
            KeyPair.class,
            createAttributes(KeyPair.class, GETTER_METHODS_ONLY, SNAKE_CASE)
    );

    @Getter
    private final KeySource source;
    /**
     * Note that keystore aliases are case-insensitive in general case
     */
    private final IndexedCollection<KeyPair> keys = new TransactionalIndexedCollection<>(KeyPair.class);

    public PairView(KeySource source) {
        this(source, Collections.emptyList());
    }

    @SneakyThrows
    public PairView(KeySource source, Collection<Index<KeyPair>> indexes) {
        this.source = source;
        keys.addAll(
                source.aliasesFor(ProvidedKeyPair.class)
                        .map(it -> new KeyPair(it.getKey(), source.asPair(it.getKey())))
                        .collect(Collectors.toList())
        );
        indexes.forEach(keys::addIndex);
    }

    @Override
    public QueryResult<KeyPair> retrieve(Query<KeyPair> query) {
        return new CqeQueryResult<>(keys.retrieve(query));
    }

    @Override
    public QueryResult<KeyPair> retrieve(String query) {
        return new CqeQueryResult<>(keys.retrieve(PARSER.parse(query).getQuery()));
    }

    @Override
    public ResultCollection<KeyPair> all() {
        return new CqeQueryResult<>(keys.retrieve(QueryFactory.all(KeyPair.class))).toCollection();
    }

    @Override
    protected String getKeyId(KeyPair ofKey) {
        return ofKey.getAlias();
    }

    @Override
    protected KeyPair viewFromId(String ofKey) {
        return new KeyPair(ofKey, source.asPair(ofKey));
    }

    @Override
    protected boolean updateCollection(Collection<KeyPair> keysToRemove, Collection<KeyPair> keysToAdd) {
        return keys.update(keysToRemove, keysToAdd);
    }
}
