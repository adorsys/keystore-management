package de.adorsys.keymanagement.bouncycastle.adapter.services.decode;

import de.adorsys.keymanagement.api.source.KeyDecoder;
import org.bouncycastle.util.Arrays;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

public class DefaultDecoderImpl implements KeyDecoder {

    @Inject
    public DefaultDecoderImpl() {
    }

    @Override
    public String decodeAsString(byte[] keyAsBytes) {
        return new String(Arrays.copyOf(keyAsBytes, keyAsBytes.length - 2), StandardCharsets.UTF_16BE);
    }
}
