package de.adorsys.keymanagement.core.collection.keystore.view;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.core.collection.keystore.KeyAlias;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.Getter;
import lombok.SneakyThrows;

import java.security.KeyStore;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Supplier;

public class AliasView extends KeyStoreUpdatingView<KeyAlias, ProvidedKeyTemplate, KeyAlias> {

    @Getter
    private final KeyStore source;
    /**
     * Note that keystore aliases are case-insensitive in general case
     */
    private final IndexedCollection<KeyAlias> aliases = new TransactionalIndexedCollection<>(KeyAlias.class);

    public AliasView(KeyStore source, KeyStoreOper oper) {
        this(source, oper, Collections.emptyList());
    }

    @SneakyThrows
    public AliasView(KeyStore source, KeyStoreOper oper, Collection<Index<KeyAlias>> indexes) {
        super(source, oper);
        this.source = source;
        Enumeration<String> ksAliases = source.aliases();
        while (ksAliases.hasMoreElements()) {
            this.aliases.add(withAlias(ksAliases.nextElement()));
        }

        this.aliases.addIndex(RadixTreeIndex.onAttribute(KeyAlias.A_ID));
        indexes.forEach(aliases::addIndex);
    }

    public ResultSet<KeyAlias> retrieve(Query<KeyAlias> query) {
        return aliases.retrieve(query);
    }

    @Override
    protected boolean updateInternal(Collection<KeyAlias> toRemove, Collection<KeyAlias> toAdd) {
        return aliases.update(toRemove, toAdd);
    }

    @Override
    protected KeyAlias readByAlias(String alias, Supplier<char[]> password) {
        return withAlias(alias);
    }

    private KeyAlias withAlias(String alias) {
        return new KeyAlias(alias);
    }
}
