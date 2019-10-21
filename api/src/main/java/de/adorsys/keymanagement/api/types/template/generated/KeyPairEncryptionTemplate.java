package de.adorsys.keymanagement.api.types.template.generated;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class KeyPairEncryptionTemplate {

    @Builder.Default
    private final String algo = "RSA";

    @Builder.Default
    private final int size = 2048;

    @Builder.Default
    private final String sigAlgo = "SHA256withRSA";

    public interface ExcludeToBuilder {
        KeyPairEncryptionTemplate toBuilder();
    }

    static KeyPairEncryptionTemplate of(String algo, Integer size, String sigAlgo) {
        KeyPairEncryptionTemplate result = KeyPairEncryptionTemplate.builder().build();

        if (null != algo) {
            result = result.toBuilder().algo(algo).build();
        }

        if (null != size) {
            result = result.toBuilder().size(size).build();
        }

        if (null != sigAlgo) {
            result = result.toBuilder().sigAlgo(sigAlgo).build();
        }

        return result;
    }
}
