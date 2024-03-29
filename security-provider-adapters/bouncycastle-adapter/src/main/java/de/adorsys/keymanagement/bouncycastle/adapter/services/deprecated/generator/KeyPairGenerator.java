package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.SelfSignedKeyPairData;
import lombok.Builder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.security.KeyPair;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;

@Builder
public class KeyPairGenerator {

    private static final int[] KEY_USAGE_SIGNATURE = {KeyUsage.nonRepudiation};
    private static final int[] KEY_USAGE_ENCRYPTION = {
            KeyUsage.keyEncipherment, KeyUsage.dataEncipherment, KeyUsage.keyAgreement
    };

    private final Provider provider;
    private final String keyAlgo;
    private final int keySize;
    private final AlgorithmParameterSpec paramSpec;
    private final String serverSigAlgo;
    private final String serverKeyPairName;

    @Builder.Default
    private final int daysAfter = 900;

    @Builder.Default
    private final boolean withCA = false;

    public KeyPairData generateSignatureKey() {
        return generate(KEY_USAGE_SIGNATURE);
    }

    public KeyPairData generateEncryptionKey() {
        return generate(KEY_USAGE_ENCRYPTION);
    }

    private KeyPairData generate(int[] keyUsages) {
        KeyPair keyPair = new KeyPairBuilder()
                .withProvider(provider)
                .withKeyAlg(keyAlgo)
                .withKeyLength(keySize)
                .withParamSpec(paramSpec)
                .build();

        X500Name dn = new X500NameBuilder(BCStyle.INSTANCE).addRDN(BCStyle.CN, serverKeyPairName).build();

        SelfSignedKeyPairData keyPairData = new SingleKeyUsageSelfSignedCertBuilder()
                .withProvider(provider)
                .withSubjectDN(dn)
                .withSignatureAlgo(serverSigAlgo)
                .withNotAfterInDays(daysAfter)
                .withCa(withCA)
                .withKeyUsages(keyUsages)
                .build(keyPair);
        return KeyPairData.builder().keyPair(keyPairData).build();
    }
}
