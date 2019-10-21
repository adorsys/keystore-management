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
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.api.view.QueryResult;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static de.adorsys.keymanagement.core.view.ViewUtil.SNAKE_CASE;

public class EntryViewImpl extends BaseUpdatingView<KeyEntry> implements EntryView<Query<KeyEntry>> {

    private static final SQLParser<KeyEntry> PARSER = SQLParser.forPojoWithAttributes(
            KeyEntry.class,
            createAttributes(KeyEntry.class, GETTER_METHODS_ONLY, SNAKE_CASE)
    );

    @Getter
    private final KeySource source;
    /**
     * Note that keystore aliases are case-insensitive in general case
     */
    private final IndexedCollection<KeyEntry> keys = new TransactionalIndexedCollection<>(KeyEntry.class);

    public EntryViewImpl(KeySource source) {
        this(source, Collections.emptyList());
    }

    @SneakyThrows
    public EntryViewImpl(KeySource source, Collection<Index<KeyEntry>> indexes) {
        this.source = source;
        keys.addAll(
                source.aliasesFor(ProvidedKeyEntry.class)
                        .map(it -> new KeyEntry(it, null, source.asEntry(it)))
                        .collect(Collectors.toList())
        );
        indexes.forEach(keys::addIndex);
    }

    @Override
    public QueryResult<KeyEntry> retrieve(Query<KeyEntry> query) {
        return new CqeQueryResult<>(keys.retrieve(query));
    }

    @Override
    public QueryResult<KeyEntry> retrieve(String query) {
        return new CqeQueryResult<>(keys.retrieve(PARSER.parse(query).getQuery()));
    }

    @Override
    public ResultCollection<KeyEntry> all() {
        return new CqeQueryResult<>(keys.retrieve(QueryFactory.all(KeyEntry.class))).toCollection();
    }

    @Override
    public QueryResult<KeyEntry> secretKeys() {
        return retrieve("SELECT * FROM keys WHERE is_secret = true");
    }

    @Override
    public QueryResult<KeyEntry> privateKeys() {
        return retrieve("SELECT * FROM keys WHERE is_private = true");
    }

    @Override
    public QueryResult<KeyEntry> trustedCerts() {
        return retrieve("SELECT * FROM keys WHERE is_trusted_cert = true");
    }

    @Override
    protected String getKeyId(KeyEntry ofKey) {
        return ofKey.getAlias();
    }

    @Override
    protected KeyEntry viewFromId(String ofKey) {
        return new KeyEntry(ofKey, null, source.asEntry(ofKey)); // FIXME fill metadata
    }

    @Override
    protected boolean updateCollection(Collection<KeyEntry> keysToRemove, Collection<KeyEntry> keysToAdd) {
        return keys.update(keysToRemove, keysToAdd);
    }
}
