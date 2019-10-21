package de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore;

import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.BaseTypeString;
import lombok.ToString;

/**
 * Wrapper that identifies key inside keystore.
 */
@ToString(callSuper = true)
public class KeyID extends BaseTypeString {

    public KeyID(String value) {
        super(value);
    }
}
