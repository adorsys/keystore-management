package de.adorsys.keymanagement.api.types.pbkdf;

import lombok.Builder;
import lombok.Getter;

/**
 * Scrypt-based key derivation.
 */
@Getter
@Builder
public class Scrypt {

    /**
     * Password derivation cost, for BCFKS refer to {@see org.bouncycastle.crypto.util.ScryptConfig}
     */
    private final int cost;
    /**
     * Password derivation block size, for BCFKS refer to {@see org.bouncycastle.crypto.util.ScryptConfig}
     */
    private final int blockSize;

    /**
     * Password derivation parallelization, for BCFKS refer to {@see org.bouncycastle.crypto.util.ScryptConfig}
     */
    private final int parallelization;

    /**
     * Password derivation salt length, for BCFKS refer to {@see org.bouncycastle.crypto.util.ScryptConfig}
     */
    private final int saltLength;
}
