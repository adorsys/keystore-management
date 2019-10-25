package de.adorsys.keymanagement.api.types.template.generated;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class PbeKeyEncryptionTemplate {

    @Builder.Default
    private final String algo = "PBEWithSHA256And256BitAES-CBC-BC";

    @Builder.Default
    private final int saltLen = 8;

    @Builder.Default
    private final int iterCount = 1024;

    public interface ExcludeToBuilder {
        PbeKeyEncryptionTemplate toBuilder();
    }

    static PbeKeyEncryptionTemplate of(String algo, Integer saltLen, Integer iterCount) {
        PbeKeyEncryptionTemplate result = PbeKeyEncryptionTemplate.builder().build();

        if (null != algo) {
            result = result.toBuilder().algo(algo).build();
        }

        if (null != saltLen) {
            result = result.toBuilder().saltLen(saltLen).build();
        }

        if (null != iterCount) {
            result = result.toBuilder().iterCount(iterCount).build();
        }

        return result;
    }
}
