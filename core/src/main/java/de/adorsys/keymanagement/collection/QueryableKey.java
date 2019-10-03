package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.attribute.Attribute;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

@Getter
@Builder
@RequiredArgsConstructor
public class QueryableKey {

    public static final Attribute<QueryableKey, String> ID = attribute("id", QueryableKey::getAlias);
    public static final Attribute<QueryableKey, KeyMetadata> META = attribute("meta", QueryableKey::getMetadata);
    public static final Attribute<QueryableKey, Boolean> IS_TRUST_CERT = attribute("is_trust_cert", it -> it.getKey() instanceof KeyStore.TrustedCertificateEntry);
    public static final Attribute<QueryableKey, Boolean> IS_SECRET = attribute("is_secret", it -> it.getKey() instanceof KeyStore.SecretKeyEntry);
    public static final Attribute<QueryableKey, Boolean> IS_PRIVATE = attribute("is_private", it -> it.getKey() instanceof KeyStore.PrivateKeyEntry);
    public static final Attribute<QueryableKey, Boolean> HAS_VALID_CERTS = attribute(
            "has_valid_certs", it -> {
                if (!(it.getKey() instanceof KeyStore.PrivateKeyEntry)) {
                    return false;
                }
                KeyStore.PrivateKeyEntry pKey = (KeyStore.PrivateKeyEntry) it.getKey();
                if (! (pKey.getCertificate() instanceof X509Certificate)) {
                    return false;
                }

                try {
                    ((X509Certificate) pKey.getCertificate()).checkValidity();
                } catch (Exception ex) {
                    return false;
                }

                return true;
            });

    private final String alias;
    private final KeyMetadata metadata;
    private final KeyStore.Entry key;

    QueryableKey(String alias, KeyStore.Entry entry) {
        this.alias = alias;
        this.metadata = null;
        this.key = entry;
    }
}
