package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.SneakyThrows;
import lombok.val;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;

// FIXME should be derived from KeyStorage
public class KeyView {

    private final KeyStore source;
    private final IndexedCollection<QueryableKey> keys = new TransactionalIndexedCollection<>(QueryableKey.class);
    private final SQLParser<QueryableKey> parser = SQLParser.forPojoWithAttributes(
            QueryableKey.class,
            createAttributes(QueryableKey.class, GETTER_METHODS_ONLY)
    );

    // FIXME key password should not be provided, but rather one should open keystore for querying
    public KeyView(KeyStore source, char[] keyPassword) {
        this.source = source;
        keys.addAll(readKeys(source, keyPassword));
        keys.addIndex(RadixTreeIndex.onAttribute(QueryableKey.ID));
        keys.addIndex(HashIndex.onAttribute(QueryableKey.IS_TRUST_CERT));
        keys.addIndex(HashIndex.onAttribute(QueryableKey.IS_PRIVATE));
        keys.addIndex(HashIndex.onAttribute(QueryableKey.IS_SECRET));
        //keys.addIndex(HashIndex.onAttribute(QueryableKey.META));
    }

    public ResultSet<QueryableKey> retrieve(Query<QueryableKey> query) {
        return keys.retrieve(query);
    }

    public ResultSet<QueryableKey> retrieve(String query) {
        return parser.retrieve(keys, query);
    }

    @SneakyThrows
    private List<QueryableKey> readKeys(KeyStore keyStore, char[] password) {
        List<QueryableKey> result = new ArrayList<>();
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement();
            val key = keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));
            result.add(new QueryableKey(alias, key));
        }

        return result;
    }
}
