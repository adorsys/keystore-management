package de.adorsys.keymanagement.core.persist;

import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.core.source.KeySet;
import de.adorsys.keymanagement.core.template.provided.ProvidedKey;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class KeyStoreOperImpl implements KeyStoreOper {

    private final Supplier<char[]> defaultKeyPassword;

    @Override
    @SneakyThrows
    public KeyStore generate(KeySet keySet) {
        KeyStore ks = KeyStore.getInstance("UBER");
        ks.load(null);

        keySet.getKeyEntries().forEach(it -> doAddToKeyStoreAndGetName(ks, it));
        keySet.getKeys().forEach(it -> doAddToKeyStoreAndGetName(ks, it));
        keySet.getKeyPairs().forEach(it -> doAddToKeyStoreAndGetName(ks, it));
        return ks;
    }

    @Override
    public String addToKeyStoreAndGetName(KeyStore ks, ProvidedKeyTemplate entry) {
        if (entry instanceof ProvidedKeyEntry) {
            return doAddToKeyStoreAndGetName(ks, (ProvidedKeyEntry) entry);
        }
        if (entry instanceof ProvidedKeyPair) {
            return doAddToKeyStoreAndGetName(ks, (ProvidedKeyPair) entry);
        }
        if (entry instanceof ProvidedKey) {
            return doAddToKeyStoreAndGetName(ks, (ProvidedKey) entry);
        }
        throw new IllegalArgumentException("Unsupported entry: " + entry.getClass());
    }

    @SneakyThrows
    private String doAddToKeyStoreAndGetName(KeyStore ks, ProvidedKeyEntry entry) {
        String name = entry.generateName();
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(getPassword(entry));
        ks.setEntry(name, entry.getEntry(), protParam);
        return name;
    }

    @SneakyThrows
    private String doAddToKeyStoreAndGetName(KeyStore ks, ProvidedKeyPair pair) {
        String name = pair.generateName();
        ks.setKeyEntry(
                name,
                pair.getPrivate(), // FIXME: Public key should be somewhere?
                getPassword(pair),
                pair.getCertificates().toArray(new Certificate[0])
        );

        return name;
    }

    @SneakyThrows
    private String doAddToKeyStoreAndGetName(KeyStore ks, ProvidedKey key) {
        String name = key.generateName();
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry((SecretKey) key.getKey());
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(getPassword(key));
        ks.setEntry(name, entry, protParam);
        return name;
    }

    private char[] getPassword(ProvidedKeyTemplate key) {
        if (null == key.getPassword()) {
            return defaultKeyPassword.get();
        }

        return key.getPassword().get();
    }
}
