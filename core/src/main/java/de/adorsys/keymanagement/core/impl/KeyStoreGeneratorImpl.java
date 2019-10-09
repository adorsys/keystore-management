package de.adorsys.keymanagement.core.impl;

import de.adorsys.keymanagement.api.KeyStoreGenerator;
import de.adorsys.keymanagement.core.KeySet;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class KeyStoreGeneratorImpl implements KeyStoreGenerator {

    private final Supplier<char[]> defaultKeyPassword;

    @Override
    @SneakyThrows
    public KeyStore generate(KeySet keySet) {
        KeyStore ks = KeyStore.getInstance("UBER");
        ks.load(null);

        keySet.getKeys().forEach(it -> addToKeyStore(ks, it));
        keySet.getKeyPairs().forEach(it -> addToKeyStore(ks, it));
        return ks;
    }

    @SneakyThrows
    private void addToKeyStore(KeyStore ks, ProvidedKeyPair pair) {
        ks.setKeyEntry(
                pair.getName(),
                pair.getPrivate(), // FIXME: Public key should be somewhere?
                getPassword(pair),
                pair.getCertificates().toArray(new Certificate[0])
        );
    }

    @SneakyThrows
    private void addToKeyStore(KeyStore ks, Provided key) {
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry((SecretKey) key.getKey());
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(getPassword(key));
        ks.setEntry(key.getName(), entry, protParam);
    }

    private char[] getPassword(ProvidedKeyTemplate key) {
        if (null == key.getPassword()) {
            return defaultKeyPassword.get();
        }

        return key.getPassword().get();
    }
}
