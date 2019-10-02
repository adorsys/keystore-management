package de.adorsys.keymanagement.template;

import lombok.experimental.SuperBuilder;

// FIXME Provider-pluggable, dagger configured
@SuperBuilder
public class KeyPair extends KeyTemplate {

    /*
    return new KeyPairGeneratorImpl("RSA", 2048, "SHA256withRSA", "enc-" + keyPrefix);
     */

   // @Builder.Default
    private final String keyAlgo = "RSA";

  //  @Builder.Default
    private final int keySize = 2048;

 //   @Builder.Default
    private final String serverSigAlgo = "SHA256withRSA";

    public static SigningKeyPair.SigningKeyPairBuilder signing() {
        return SigningKeyPair.builder();
    }

    public static EncryptingKeyPair.EncryptingKeyPairBuilder encrypting() {
        return EncryptingKeyPair.builder();
    }
}
