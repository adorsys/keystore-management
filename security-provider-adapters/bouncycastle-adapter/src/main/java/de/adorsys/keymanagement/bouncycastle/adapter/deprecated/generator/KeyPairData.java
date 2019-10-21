package de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.KeyPairEntry;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.SelfSignedKeyPairData;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KeyPairData extends KeyEntryData implements KeyPairEntry {

    private final SelfSignedKeyPairData keyPair;

    @Builder
    private KeyPairData(ReadKeyPassword readKeyPassword, String alias, SelfSignedKeyPairData keyPair) {
        super(readKeyPassword, alias);
        this.keyPair = keyPair;
    }
}
