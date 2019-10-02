package de.adorsys.keymanagement.generator.generator;

import de.adorsys.keymanagement.generator.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.generator.types.KeyPairEntry;
import de.adorsys.keymanagement.generator.types.SelfSignedKeyPairData;
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
