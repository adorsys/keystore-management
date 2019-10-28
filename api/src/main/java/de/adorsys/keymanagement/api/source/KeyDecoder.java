package de.adorsys.keymanagement.api.source;

public interface KeyDecoder {

    // Decodes key bytes into string
    String decodeAsString(byte[] keyAsBytes);
}
