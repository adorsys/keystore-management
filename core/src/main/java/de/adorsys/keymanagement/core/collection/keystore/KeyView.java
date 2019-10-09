package de.adorsys.keymanagement.core.collection.keystore;

import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.api.ModifiableView;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.*;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;
import static com.googlecode.cqengine.codegen.MemberFilters.GETTER_METHODS_ONLY;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static de.adorsys.keymanagement.core.collection.keystore.QueryableKey.*;

// FIXME should be derived from KeyStorage
public class KeyView implements ModifiableView<QueryableKey, ProvidedKeyTemplate> {

    @Getter
    private final KeyStore source;
    private final KeyStoreOper oper;

    private final IndexedCollection<QueryableKey> keys = new TransactionalIndexedCollection<>(QueryableKey.class);
    private final SQLParser<QueryableKey> parser = SQLParser.forPojoWithAttributes(
            QueryableKey.class,
            createAttributes(QueryableKey.class, GETTER_METHODS_ONLY)
    );

    // FIXME key password should not be provided, but rather one should open keystore for querying
    public KeyView(KeyStore source, KeyStoreOper oper, char[] keyPassword) {
        this(source, oper, keyPassword, Collections.emptyList());
    }

    // FIXME key password should not be provided, but rather one should open keystore for querying
    public KeyView(KeyStore source, KeyStoreOper oper, char[] keyPassword, Collection<Index<QueryableKey>> indexes) {
        this.source = source;
        this.oper = oper;
        keys.addAll(readKeys(keyPassword));
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

    @Override
    public boolean update(Collection<QueryableKey> objectsToRemove, Collection<ProvidedKeyTemplate> objectsToAdd) {
        objectsToRemove.forEach(this::deleteEntryFromKeystore);
        Map<String, ProvidedKeyTemplate> addedKeys = new HashMap<>();
        objectsToAdd.forEach(it -> addKey(addedKeys, it));

        return keys.update(
                objectsToRemove,
                addedKeys.entrySet().stream().map(
                        it -> keyByAlias(
                                it.getKey(),
                                it.getValue().getPassword().get())
                ).collect(Collectors.toList())
        );
    }

    private void addKey(Map<String, ProvidedKeyTemplate> addedKeys, ProvidedKeyTemplate it) {
        if (it instanceof Provided) {
            addedKeys.put(oper.addToKeyStoreAndGetName(source, (Provided) it), it);
        } else if (it instanceof ProvidedKeyPair) {
            addedKeys.put(oper.addToKeyStoreAndGetName(source, (ProvidedKeyPair) it), it);
        } else if (it instanceof ProvidedKeyEntry) {
            addedKeys.put(oper.addToKeyStoreAndGetName(source, (ProvidedKeyEntry) it), it);
        } else {
            throw new IllegalArgumentException("Unknown type of key to add: " + it.getClass());
        }
    }

    @SneakyThrows
    private List<QueryableKey> readKeys(char[] password) {
        val result = new ArrayList<QueryableKey>();
        val aliases = source.aliases();
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement();
            result.add(keyByAlias(alias, password));
        }

        return result;
    }

    @SneakyThrows
    private QueryableKey keyByAlias(String alias, char[] password) {
        val key = source.getEntry(alias, new KeyStore.PasswordProtection(password));
        return new QueryableKey(alias, key);
    }

    @SneakyThrows
    private void deleteEntryFromKeystore(QueryableKey toRemove) {
        source.deleteEntry(toRemove.getAlias());
    }
}
