package de.adorsys.keymanagement.core.template.generated;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class SecretKeyEncryptionTemplate {

    @Builder.Default
    private final String algo = "AES";

    @Builder.Default
    private final int size = 256;

    public interface ExcludeToBuilder {
        SecretKeyEncryptionTemplate toBuilder();
    }

    static SecretKeyEncryptionTemplate of(String algo, Integer size) {
        SecretKeyEncryptionTemplate result = SecretKeyEncryptionTemplate.builder().build();

        if (null != algo) {
            result = result.toBuilder().algo(algo).build();
        }

        if (null != size) {
            result = result.toBuilder().size(size).build();
        }

        return result;
    }
}
