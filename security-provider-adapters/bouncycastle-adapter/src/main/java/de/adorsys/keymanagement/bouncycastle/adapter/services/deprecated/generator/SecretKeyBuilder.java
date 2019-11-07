package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.List;

public class SecretKeyBuilder {

    private Provider provider;
    private Integer keyLength;
    private String keyAlg;

    boolean dirty = false;

    /**
     * Returns the message key pair subject certificate holder.
     * <p>
     * Following entity must be validated
     *
     * @return KeyPairAndCertificateHolder
     */
    @SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    public SecretKey build() {
        if (dirty) {
            throw new IllegalStateException("Builder can not be reused");
        }

        dirty = true;
        List<KeyValue> notNullCheckList = ListOfKeyValueBuilder.newBuilder()
                .add("provider", provider)
                .add("keyAlg", keyAlg)
                .add("keyLength", keyLength)
                .build();
        List<String> nullList = BatchValidator.filterNull(notNullCheckList);

        if (nullList != null && !nullList.isEmpty()) {
            throw new IllegalArgumentException("Fields can not be null: " + nullList);
        }

        KeyGenerator kGen;
        try {
            kGen = KeyGenerator.getInstance(keyAlg, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        kGen.init(keyLength);
        return kGen.generateKey();
    }

    public SecretKeyBuilder withProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public SecretKeyBuilder withKeyLength(Integer keyLength) {
        this.keyLength = keyLength;
        return this;
    }

    public SecretKeyBuilder withKeyAlg(String keyAlg) {
        this.keyAlg = keyAlg;
        return this;
    }

}
