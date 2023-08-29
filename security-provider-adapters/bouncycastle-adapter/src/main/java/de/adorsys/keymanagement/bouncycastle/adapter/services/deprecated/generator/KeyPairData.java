package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.KeyPairEntry;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.SelfSignedKeyPairData;
import lombok.Builder;
import lombok.Getter;

@Getter
@SuppressWarnings("checkstyle:FinalClass")
public class KeyPairData implements KeyPairEntry {

    private final SelfSignedKeyPairData keyPair;

    @Builder
    private KeyPairData(SelfSignedKeyPairData keyPair) {
        this.keyPair = keyPair;
    }
}
