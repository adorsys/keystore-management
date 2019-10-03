package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.attribute.Attribute;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.KeyStore;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

@Getter
@Builder
@RequiredArgsConstructor
public class QueryableKey {

    public static final Attribute<QueryableKey, String> ID = attribute("id", QueryableKey::getAlias);
    public static final Attribute<QueryableKey, KeyMetadata> META = attribute("meta", QueryableKey::getMetadata);
    public static final Attribute<QueryableKey, Boolean> IS_TRUST_CERT = attribute("is_trust_cert", it -> it.getKey().get() instanceof KeyStore.TrustedCertificateEntry);
    public static final Attribute<QueryableKey, Boolean> IS_SECRET = attribute("is_secret", it -> it.getKey().get() instanceof KeyStore.SecretKeyEntry);
    public static final Attribute<QueryableKey, Boolean> IS_PRIVATE = attribute("is_private", it -> it.getKey().get() instanceof KeyStore.PrivateKeyEntry);

    private final String alias;
    private final KeyMetadata metadata;
    private final AtomicReference<KeyStore.Entry> key = new AtomicReference<>();

    QueryableKey(String alias, KeyStore.Entry entry) {
        this.alias = alias;
        this.metadata = null;
        this.key.set(entry);
    }
}
