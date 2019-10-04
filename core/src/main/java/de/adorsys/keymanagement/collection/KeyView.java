package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import lombok.SneakyThrows;
import lombok.val;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.*;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.collection.QueryableKey.*;

// FIXME try-with-resources on retrieve
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
        this(source, keyPassword, Collections.emptyList());
    }

    // FIXME key password should not be provided, but rather one should open keystore for querying
    public KeyView(KeyStore source, char[] keyPassword, Collection<Index<QueryableKey>> indexes) {
        this.source = source;
        keys.addAll(readKeys(source, keyPassword));
        keys.addIndex(RadixTreeIndex.onAttribute(QueryableKey.ID));
        keys.addIndex(HashIndex.onAttribute(IS_TRUST_CERT));
        keys.addIndex(HashIndex.onAttribute(IS_PRIVATE));
        keys.addIndex(HashIndex.onAttribute(QueryableKey.IS_SECRET));
        keys.addIndex(HashIndex.onAttribute(QueryableKey.CERT));
        indexes.forEach(keys::addIndex);
        //keys.addIndex(HashIndex.onAttribute(QueryableKey.META));
    }

    public QueryResult<QueryableKey> retrieve(Query<QueryableKey> query) {
        return new QueryResult<>(keys.retrieve(query));
    }

    public QueryResult<QueryableKey> retrieve(String query) {
        return new QueryResult<>(parser.retrieve(keys, query));
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
