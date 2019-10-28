package de.adorsys.keymanagement.api.source;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public interface KeyDecoder {

    /**
     * Decodes {@link SecretKeySpec#getEncoded()} PBE-key bytes into String representation.
     * @param keyAsBytes Encoded bytes.
     * @return String value which was available from {@link PBEKeySpec#getPassword()} before key was saved to KeyStore.
     */
    String decodeAsString(byte[] keyAsBytes);
}
