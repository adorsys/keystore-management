package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.SelfSignedKeyPairData;
import lombok.Builder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.security.KeyPair;

@Builder
public class KeyPairGenerator {

    private static final int[] keyUsageSignature = {KeyUsage.nonRepudiation};
    private static final int[] keyUsageEncryption = {KeyUsage.keyEncipherment, KeyUsage.dataEncipherment, KeyUsage.keyAgreement};

    private final String keyAlgo;
    private final int keySize;
    private final String serverSigAlgo;
    private final String serverKeyPairName;

    @Builder.Default
    private final int daysAfter = 900;

    @Builder.Default
    private final boolean withCA = false;

    public KeyPairData generateSignatureKey() {
        return generate(keyUsageSignature);
    }

    public KeyPairData generateEncryptionKey() {
        return generate(keyUsageEncryption);
    }

    private KeyPairData generate(int[] keyUsages) {
        KeyPair keyPair = new KeyPairBuilder().withKeyAlg(keyAlgo).withKeyLength(keySize).build();
        X500Name dn = new X500NameBuilder(BCStyle.INSTANCE).addRDN(BCStyle.CN, serverKeyPairName).build();
        SelfSignedKeyPairData keyPairData = new SingleKeyUsageSelfSignedCertBuilder()
                .withSubjectDN(dn)
                .withSignatureAlgo(serverSigAlgo)
                .withNotAfterInDays(daysAfter)
                .withCa(withCA)
                .withKeyUsages(keyUsages)
                .build(keyPair);
        return KeyPairData.builder().keyPair(keyPairData).build();
    }
}
