package de.adorsys.keymanagement.bouncycastle.adapter.services.serde;

import de.adorsys.keymanagement.api.config.keystore.KeyStoreConfig;
import de.adorsys.keymanagement.api.persist.SerDe;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.util.function.Supplier;

public class DefaultKeyStoreSerde implements SerDe {

    private final Provider provider;
    private final KeyStoreConfig config;

    @Inject
    public DefaultKeyStoreSerde(Provider provider, @Nullable KeyStoreConfig config) {
        this.provider = provider;
        this.config = null == config ? KeyStoreConfig.builder().build() : config;
    }

    @Override
    public DefaultKeyStoreSerde withConfig(KeyStoreConfig config) {
        return new DefaultKeyStoreSerde(provider, config);
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
        KeyStore ks = KeyStore.getInstance(config.getType(), provider);
        ks.load(new ByteArrayInputStream(keyStore), keyStorePassword.get());
        return ks;
    }
}
