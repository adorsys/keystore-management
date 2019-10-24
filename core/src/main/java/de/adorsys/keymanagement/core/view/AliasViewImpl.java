package de.adorsys.keymanagement.core.view;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import de.adorsys.keymanagement.api.CqeQueryResult;
import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;
import de.adorsys.keymanagement.api.types.entity.WithMetadata;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.view.AliasView;
import de.adorsys.keymanagement.api.view.QueryResult;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static com.googlecode.cqengine.query.QueryFactory.*;
import static de.adorsys.keymanagement.core.view.ViewUtil.SNAKE_CASE;

public class AliasViewImpl extends BaseUpdatingView<Query<KeyAlias>, KeyAlias> implements AliasView<Query<KeyAlias>> {

    public static final SimpleAttribute<KeyAlias, String> A_ID = attribute("alias", KeyAlias::getAlias);
    public static final SimpleNullableAttribute<KeyAlias, KeyMetadata> META = nullableAttribute("meta", KeyAlias::getMeta);
    public static final SimpleAttribute<KeyAlias, Boolean> IS_META = attribute("is_meta", KeyAlias::isMetadataEntry);

    private static final SQLParser<KeyAlias> PARSER = SQLParser.forPojoWithAttributes(
            KeyAlias.class,
            createAttributes(KeyAlias.class, GETTER_METHODS_ONLY, SNAKE_CASE)
    );

    @Getter
    private final KeySource source;

    private final Query<KeyAlias> viewFilter;

    /**
     * Note that keystore aliases are case-insensitive in general case
     */
    private final IndexedCollection<KeyAlias> aliases = new TransactionalIndexedCollection<>(KeyAlias.class);

    @SneakyThrows
    public AliasViewImpl(KeySource source, Query<KeyAlias> viewFilter, Collection<Index<KeyAlias>> indexes) {
        this.source = source;
        this.viewFilter = viewFilter;

        aliases.addAll(
                source.aliases()
                        .map(it -> new KeyAlias(it.getKey(), it.getMetadata(), it.isMetadataEntry()))
                        .collect(Collectors.toList())
        );

        this.aliases.addIndex(RadixTreeIndex.onAttribute(A_ID));
        this.aliases.addIndex(HashIndex.onAttribute(META));
        this.aliases.addIndex(HashIndex.onAttribute(IS_META));
        indexes.forEach(aliases::addIndex);
    }

    @Override
    public QueryResult<KeyAlias> retrieve(Query<KeyAlias> query) {
        return new CqeQueryResult<>(aliases.retrieve(and(viewFilter, query)));
    }

    @Override
    public QueryResult<KeyAlias> retrieve(String query) {
        return new CqeQueryResult<>(aliases.retrieve(and(viewFilter, PARSER.parse(query).getQuery())));
    }

    @Override
    public ResultCollection<KeyAlias> all() {
        return new CqeQueryResult<>(aliases.retrieve(viewFilter)).toCollection();
    }

    @Override
    protected String getKeyId(KeyAlias ofKey) {
        return ofKey.getAlias();
    }

    @Override
    protected KeyAlias newViewFromId(String ofKey) {
        WithMetadata<String> key = source.asAliasWithMeta(ofKey);
        return new KeyAlias(ofKey, key.getMetadata(), key.isMetadataEntry());
    }

    @Override
    protected KeyAlias getViewFromId(String ofKey) {
        return retrieve(equal(A_ID, ofKey)).toCollection().first();
    }

    @Override
    protected boolean updateCollection(Collection<KeyAlias> keysToRemove, Collection<KeyAlias> keysToAdd) {
        return aliases.update(keysToRemove, keysToAdd);
    }
}
