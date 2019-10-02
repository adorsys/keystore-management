package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.SneakyThrows;

import java.security.KeyStore;
import java.util.Enumeration;

public class AliasCollection {

    private final IndexedCollection<WithAlias> aliases = new TransactionalIndexedCollection<>(WithAlias.class);

    @SneakyThrows
    public AliasCollection(KeyStore keyStore) {
        Enumeration<String> ksAliases = keyStore.aliases();
        while (ksAliases.hasMoreElements()) {
            this.aliases.add(new WithAlias(ksAliases.nextElement()));
        }

        this.aliases.addIndex(RadixTreeIndex.onAttribute(WithAlias.A_ID));
    }

    public ResultSet<WithAlias> retrieve(Query<WithAlias> query) {
        return aliases.retrieve(query);
    }
}
