package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.keystore;

import lombok.*;

/**
 * Authenticated path encryption secret key holder.
 */
@Getter
@ToString
@Value
@RequiredArgsConstructor
public class AuthPathEncryptionSecretKey {

    @NonNull
    private final SecretKeyIDWithKey secretKey;

    @NonNull
    private final SecretKeyIDWithKey counterSecretKey;
}
