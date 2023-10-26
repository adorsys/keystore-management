package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.spec.ECParameterSpec;
import java.util.List;

/**
 * Instantiates and returns a key pair certificate.
 *
 * @author fpo
 */
public class KeyPairBuilder {

    private Provider provider;
    private Integer keyLength;
    private String keyAlg;
//    private AlgorithmParameterSpec paramSpec;

    boolean dirty = false;

    /**
     * Returns the message key pair subject certificate holder.
     *
     * @return KeyPair
     */
    @SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    public KeyPair build() {
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
        // Generate a key pair for the new EndEntity
        KeyPairGenerator kGen;
        try {
            kGen = KeyPairGenerator.getInstance(keyAlg, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }

        if ("ECDH".equals(keyAlg)) {
            X9ECParameters ecP = CustomNamedCurves.getByName("Curve25519");
            ECParameterSpec paramSpec = EC5Util.convertToSpec(ecP);
            try {
                kGen.initialize(paramSpec);
            } catch (InvalidAlgorithmParameterException e) {
                throw new RuntimeException(e);
            }
        } else { // RSA
            kGen.initialize(keyLength);
        }



        return kGen.generateKeyPair();
    }

    public KeyPairBuilder withProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public KeyPairBuilder withKeyLength(Integer keyLength) {
        this.keyLength = keyLength;
        return this;
    }

    public KeyPairBuilder withKeyAlg(String keyAlg) {
        this.keyAlg = keyAlg;
        return this;
    }
}
