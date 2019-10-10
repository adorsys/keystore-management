package de.adorsys.keymanagement.core.collection.keystore.view;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.core.collection.keystore.FilterableCollection;
import de.adorsys.keymanagement.core.collection.keystore.QueryResult;
import de.adorsys.keymanagement.core.collection.keystore.QueryableKey;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.SneakyThrows;
import lombok.val;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.core.collection.keystore.QueryableKey.*;

// FIXME should be derived from KeyStorage
public class KeyView extends KeyStoreUpdatingView<QueryableKey, ProvidedKeyTemplate, QueryableKey> {

    private static final SQLParser<QueryableKey> PARSER = SQLParser.forPojoWithAttributes(
            QueryableKey.class,
            createAttributes(QueryableKey.class, GETTER_METHODS_ONLY)
    );

    private final IndexedCollection<QueryableKey> keys = new TransactionalIndexedCollection<>(QueryableKey.class);

    // FIXME key password should not be provided, but rather one should open keystore for querying
    public KeyView(KeyStore source, KeyStoreOper oper, char[] keyPassword) {
        this(source, oper, keyPassword, Collections.emptyList());
    }

    // FIXME key password should not be provided, but rather one should open keystore for querying
    public KeyView(KeyStore source, KeyStoreOper oper, char[] keyPassword, Collection<Index<QueryableKey>> indexes) {
        super(source, oper);
        keys.addAll(readKeys(() -> keyPassword));
        keys.addIndex(RadixTreeIndex.onAttribute(ID));
        keys.addIndex(HashIndex.onAttribute(IS_TRUST_CERT));
        keys.addIndex(HashIndex.onAttribute(IS_PRIVATE));
        keys.addIndex(HashIndex.onAttribute(QueryableKey.IS_SECRET));
        keys.addIndex(HashIndex.onAttribute(QueryableKey.CERT));
        indexes.forEach(keys::addIndex);
        //keys.addIndex(HashIndex.onAttribute(QueryableKey.META));
    }

    /**
     * Note that client who calls this should close the result.
     */
    public QueryResult<QueryableKey> retrieve(Query<QueryableKey> query) {
        return new QueryResult<>(keys.retrieve(query));
    }

    /**
     * Note that client who calls this should close the result.
     */
    public QueryResult<QueryableKey> retrieve(String query) {
        return new QueryResult<>(PARSER.retrieve(keys, query));
    }

    public FilterableCollection<KeyStore.PrivateKeyEntry, PrivateKey> privateKeys() {
        try (val privateKeys = keys.retrieve(equal(IS_PRIVATE, true))) {
            return new FilterableCollection<>(privateKeys, KeyStore.PrivateKeyEntry::getPrivateKey);
        }
    }

    public FilterableCollection<KeyStore.SecretKeyEntry, SecretKey> secretKeys() {
        try (val secretKeys = keys.retrieve(equal(IS_SECRET, true))) {
            return new FilterableCollection<>(secretKeys, KeyStore.SecretKeyEntry::getSecretKey);
        }
    }

    public FilterableCollection<KeyStore.PrivateKeyEntry, PublicKey> publicKeys() {
        try (val publicKeys = keys.retrieve(equal(IS_PRIVATE, true))) {
            return new FilterableCollection<>(publicKeys, e -> e.getCertificate().getPublicKey());
        }
    }

    public FilterableCollection<KeyStore.TrustedCertificateEntry, Certificate> trustedCerts() {
        try (val trustedCerts = keys.retrieve(equal(IS_TRUST_CERT, true))) {
            return new FilterableCollection<>(trustedCerts, KeyStore.TrustedCertificateEntry::getTrustedCertificate);
        }
    }

    @Override
    protected boolean updateInternal(Collection<QueryableKey> toRemove, Collection<QueryableKey> toAdd) {
        return keys.update(toRemove, toAdd);
    }

    @Override
    @SneakyThrows
    protected QueryableKey readByAlias(String alias, Supplier<char[]> password) {
        val key = getSource().getEntry(alias, new KeyStore.PasswordProtection(password.get()));
        return new QueryableKey(alias, key);
    }

    @SneakyThrows
    private List<QueryableKey> readKeys(Supplier<char[]> password) {
        val result = new ArrayList<QueryableKey>();
        val aliases = getSource().aliases();
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement();
            result.add(readByAlias(alias, password));
        }

        return result;
    }
}
