package de.adorsys.keymanagement.core.deprecated.types.keystore;

import de.adorsys.keymanagement.core.deprecated.types.BaseTypeString;
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
