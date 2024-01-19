package de.adorsys.keymanagement.core.view;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.attribute.support.SimpleFunction;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import de.adorsys.keymanagement.api.CqeQueryResult;
import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.api.view.QueryResult;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Map;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.attribute;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.nullableAttribute;


public class EntryViewImpl extends BaseUpdatingView<Query<KeyEntry>, KeyEntry> implements EntryView<Query<KeyEntry>> {

    public static final SimpleAttribute<KeyEntry, String> A_ID = attribute(
            KeyEntry.class, String.class, "alias", KeyEntry::getAlias
    );
    public static final SimpleNullableAttribute<KeyEntry, KeyMetadata> META = nullableAttribute(
            KeyEntry.class, KeyMetadata.class, "meta", (SimpleFunction<KeyEntry, KeyMetadata>) KeyEntry::getMeta
    );
    public static final SimpleAttribute<KeyEntry, Boolean> IS_META = attribute(
            KeyEntry.class, Boolean.class, "is_meta", KeyEntry::isMetadataEntry
    );

    private static final SQLParser<KeyEntry> PARSER = SQLParser.forPojoWithAttributes(
            KeyEntry.class,
            Map.<String, Attribute<KeyEntry, ?>>of(
                    "alias", A_ID,
                    "meta", META,
                    "is_meta", IS_META,
                    "is_private", attribute(KeyEntry.class, Boolean.class, "is_private", KeyEntry::isPrivate),
                    "is_secret", attribute(KeyEntry.class, Boolean.class, "is_secret", KeyEntry::isSecret),
                    "is_trusted_cert", attribute(KeyEntry.class, Boolean.class, "is_trusted_cert",
                            KeyEntry::isTrustedCert)
            )
    );

    @Getter
    private final KeySource source;

    private final Query<KeyEntry> viewFilter;

    /**
     * Note that keystore aliases are case-insensitive in general case
     */
    private final IndexedCollection<KeyEntry> keys = new TransactionalIndexedCollection<>(KeyEntry.class);

    @SneakyThrows
    public EntryViewImpl(KeySource source, Query<KeyEntry> viewFilter, Collection<Index<KeyEntry>> indexes) {
        this.source = source;
        this.viewFilter = viewFilter;

        keys.addAll(
                source.aliasesFor(ProvidedKeyEntry.class)
                        .map(it -> new KeyEntry(it.getKey(), source.asEntry(it.getKey())))
                        .toList()
        );

        this.keys.addIndex(RadixTreeIndex.onAttribute(A_ID));
        this.keys.addIndex(HashIndex.onAttribute(META));
        this.keys.addIndex(HashIndex.onAttribute(IS_META));
        indexes.forEach(keys::addIndex);
    }

    @Override
    public QueryResult<KeyEntry> retrieve(Query<KeyEntry> query) {
        return new CqeQueryResult<>(keys.retrieve(and(viewFilter, query)));
    }

    @Override
    public QueryResult<KeyEntry> retrieve(String query) {
        return new CqeQueryResult<>(keys.retrieve(and(viewFilter, PARSER.parse(query).getQuery())));
    }

    @Override
    public KeyEntry uniqueResult(Query<KeyEntry> query) {
        try (ResultSet<KeyEntry> unique = keys.retrieve(and(viewFilter, query))) {
            return unique.uniqueResult();
        }
    }

    @Override
    public KeyEntry uniqueResult(String query) {
        try (ResultSet<KeyEntry> unique = keys.retrieve(and(viewFilter, PARSER.query(query)))) {
            return unique.uniqueResult();
        }
    }

    @Override
    public ResultCollection<KeyEntry> all() {
        return new CqeQueryResult<>(keys.retrieve(viewFilter)).toCollection();
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
    protected KeyEntry fromSource(String ofKey) {
        return new KeyEntry(ofKey, source.asEntry(ofKey));
    }

    @Override
    protected KeyEntry fromCollection(String ofKey) {
        // Skip view filter
        try (ResultSet<KeyEntry> byId = keys.retrieve(equal(A_ID, ofKey))) {
            return byId.uniqueResult();
        }
    }

    @Override
    protected boolean updateCollection(Collection<KeyEntry> keysToRemove, Collection<KeyEntry> keysToAdd) {
        return keys.update(keysToRemove, keysToAdd);
    }
}
