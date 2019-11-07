package de.adorsys.keymanagement.api.source;

public interface KeyDecoder {

    /**
     * Decodes {@link javax.crypto.spec.SecretKeySpec#getEncoded()} PBE-key bytes into String representation.
     * @param keyAsBytes Encoded bytes.
     * @return String value which was available from {@link javax.crypto.spec.PBEKeySpec#getPassword()}
     * before key was saved to KeyStore.
     */
    String decodeAsString(byte[] keyAsBytes);
}
