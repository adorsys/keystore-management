package de.adorsys.keymanagement.api.types.template.generated;

import lombok.Builder;
import lombok.Getter;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Locale;

@Getter
@Builder(toBuilder = true)
public class KeyPairEncryptionTemplate {

    @Builder.Default
    private final String algo = "RSA";

    @Builder.Default
    private final int size = 2048;

    @Builder.Default
    private final String sigAlgo = "SHA256withRSA";

    @Builder.Default
    private final String commonName = "KEY-PAIR";

    @Builder.Default
    private final AlgorithmParameterSpec paramSpec = null;

    public interface ExcludeToBuilder {
        KeyPairEncryptionTemplate toBuilder();
    }

    @SuppressWarnings("checkstyle:ParameterNumber") // Is a builder method
    static KeyPairEncryptionTemplate of(String algo, Integer size, String sigAlgo, String commonName,
                                        AlgorithmParameterSpec paramSpec) {
        KeyPairEncryptionTemplate result = KeyPairEncryptionTemplate.builder().build();

        if (null != algo) {
            result = result.toBuilder().algo(algo).build();

            if (null == sigAlgo) {
                sigAlgo = autodetectAlgorithm(algo);
            }
        }

        if (null != size) {
            result = result.toBuilder().size(size).build();
        }

        if (null != sigAlgo) {
            result = result.toBuilder().sigAlgo(sigAlgo).build();
        }

        if (null != commonName) {
            result = result.toBuilder().commonName(commonName).build();
        }

        if (null != paramSpec) {
            result = result.toBuilder().paramSpec(paramSpec).build();
        }

        return result;
    }

    private static String autodetectAlgorithm(String algo) {
        return switch (algo.toUpperCase(Locale.US)) {
            case "DSA" -> "SHA256withDSA";
            case "RSA" -> "SHA256WithRSA";
            case "ECDH" -> "SHA256WITHECDSA";
            default -> null;
        };
    }
}
