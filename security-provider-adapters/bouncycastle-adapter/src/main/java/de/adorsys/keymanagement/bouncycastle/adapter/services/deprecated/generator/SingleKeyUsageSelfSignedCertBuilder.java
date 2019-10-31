package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.SelfSignedKeyPairData;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;

import java.security.KeyPair;
import java.security.Provider;
import java.util.List;

/**
 * Generate a self signed certificate. Returns the alias of the key pair.
 *
 * @author fpo
 */
public class SingleKeyUsageSelfSignedCertBuilder {

    private Provider provider;
    private String signatureAlgo;
    private Integer notAfterInDays;
    private Integer notBeforeInDays = 0;
    private X500Name subjectDN;
    private boolean ca;
    private int[] keyUsages;

    boolean dirty = false;

    /**
     * Returns the message key pair subject certificate holder.
     * <p>
     * Following entity must be validated
     * <p>
     * Will generate a self signed key pair. If there is no UniqueIdentifier in the provided
     * subjectDN, the generated public key identifier will be used for that purpose
     * and for the subjectUniqueID of the certificate. Same applies for the issuer fields.
     *
     * @param keyPair keyPair
     * @return SelfSignedKeyPairData
     */
    public SelfSignedKeyPairData build(KeyPair keyPair) {
        if (dirty) throw new IllegalStateException("Builder can not be reused");
        dirty = true;
        List<KeyValue> notNullCheckList = ListOfKeyValueBuilder.newBuilder()
                .add("provider", provider)
                .add("subjectDN", subjectDN)
                .add("signatureAlgo", signatureAlgo)
                .add("notBeforeInDays", notBeforeInDays)
                .add("notAfterInDays", notAfterInDays)
                .add("keyPair", keyPair)
                .build();

        List<String> nullList = BatchValidator.filterNull(notNullCheckList);
        if (nullList != null && !nullList.isEmpty()) {
            throw new IllegalArgumentException("Fields can not be null: " + nullList);
        }

        CaSignedCertificateBuilder builder = new CaSignedCertificateBuilder()
                .withProvider(provider)
                .withCa(ca)
                .withNotBeforeInDays(notBeforeInDays)
                .withNotAfterInDays(notAfterInDays)
                .withSubjectDN(subjectDN)
                .withSubjectPublicKey(keyPair.getPublic());
        if (keyUsages != null)
            for (int keyUsage : keyUsages) builder = builder.withKeyUsage(keyUsage);

        X509CertificateHolder subjectCert = builder.build(keyPair.getPrivate());

        return new SelfSignedKeyPairData(keyPair, subjectCert);
    }

    public SingleKeyUsageSelfSignedCertBuilder withProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public SingleKeyUsageSelfSignedCertBuilder withSubjectDN(X500Name subjectDN) {
        this.subjectDN = subjectDN;
        return this;
    }

    public SingleKeyUsageSelfSignedCertBuilder withSignatureAlgo(String signatureAlgo) {
        this.signatureAlgo = signatureAlgo;
        return this;
    }

    public SingleKeyUsageSelfSignedCertBuilder withNotAfterInDays(Integer notAfterInDays) {
        this.notAfterInDays = notAfterInDays;
        return this;
    }

    public SingleKeyUsageSelfSignedCertBuilder withNotBeforeInDays(Integer notBeforeInDays) {
        this.notBeforeInDays = notBeforeInDays;
        return this;
    }

    public SingleKeyUsageSelfSignedCertBuilder withCa(boolean ca) {
        this.ca = ca;
        return this;
    }

    public SingleKeyUsageSelfSignedCertBuilder withKeyUsages(int[] keyUsages) {
        this.keyUsages = keyUsages;
        return this;
    }
}
