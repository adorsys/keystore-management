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
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.*;
import java.util.stream.Collectors;

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
        keys.addIndex(HashIndex.onAttribute(QueryableKey.IS_TRUST_CERT));
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

    public PrivateKeyFilterableCollection privateKeys() {
        try (val privateKeys = keys.retrieve(equal(IS_PRIVATE, true))) {
            return new PrivateKeyFilterableCollection(privateKeys);
        }
    }

    public ResultCollection<SecretKey> secretKeys() {
        try (val secretKeys = keys.retrieve(equal(IS_SECRET, true)).stream()) {
            return secretKeys
                    .map(it -> ((KeyStore.SecretKeyEntry) it.getKey()).getSecretKey())
                    .collect(
                            Collectors.toCollection(() -> new ResultCollection<>(new LinkedHashSet<>()))
                    );
        }
    }

    public ResultCollection<PublicKey> publicKeys() {
        try (val privateKeys = keys.retrieve(equal(IS_PRIVATE, true)).stream()) {
            return privateKeys
                    .map(it -> ((KeyStore.PrivateKeyEntry) it.getKey()).getCertificate())
                    .map(Certificate::getPublicKey)
                    .collect(
                            Collectors.toCollection(() -> new ResultCollection<>(new LinkedHashSet<>()))
                    );
        }
    }

    public ResultCollection<Certificate> trustedCerts() {
        try (val trustedCerts = keys.retrieve(equal(IS_TRUST_CERT, true)).stream()) {
            return trustedCerts
                    .map(it -> ((KeyStore.TrustedCertificateEntry) it.getKey()).getTrustedCertificate())
                    .collect(
                            Collectors.toCollection(() -> new ResultCollection<>(new LinkedHashSet<>()))
                    );
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
