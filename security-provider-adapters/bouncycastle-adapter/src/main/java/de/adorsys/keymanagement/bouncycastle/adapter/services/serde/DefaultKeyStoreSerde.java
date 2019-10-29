package de.adorsys.keymanagement.bouncycastle.adapter.services.serde;

import de.adorsys.keymanagement.api.persist.SerDe;
import de.adorsys.keymanagement.config.keystore.KeyStoreConfig;
import lombok.SneakyThrows;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.util.function.Supplier;

public class DefaultKeyStoreSerde implements SerDe {

    private final KeyStoreConfig config;

    @Inject
    public DefaultKeyStoreSerde(KeyStoreConfig config) {
        this.config = config;
    }

    @Override
    public DefaultKeyStoreSerde withConfig(KeyStoreConfig config) {
        return new DefaultKeyStoreSerde(config);
    }

    @Override
    @SneakyThrows
    public byte[] serialize(KeyStore keyStore, Supplier<char[]> keyStorePassword) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        keyStore.store(os, keyStorePassword.get());
        return os.toByteArray();
    }

    @Override
    @SneakyThrows
    public KeyStore deserialize(byte[] keyStore, Supplier<char[]> keyStorePassword) {
        KeyStore ks = KeyStore.getInstance(config.getType());
        ks.load(new ByteArrayInputStream(keyStore), keyStorePassword.get());
        return ks;
    }
}
