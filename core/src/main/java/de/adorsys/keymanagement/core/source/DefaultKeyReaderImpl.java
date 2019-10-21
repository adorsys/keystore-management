package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.KeyReader;
import de.adorsys.keymanagement.api.KeySource;
import de.adorsys.keymanagement.api.KeyStoreOper;

import javax.inject.Inject;
import java.security.KeyStore;
import java.util.function.Function;

public class DefaultKeyReaderImpl implements KeyReader {

    private final KeyStoreOper oper;

    @Inject
    public DefaultKeyReaderImpl(KeyStoreOper oper) {
        this.oper = oper;
    }

    @Override
    public KeySource fromKeyStore(KeyStore keyStore, Function<String, char[]> keyPassword) {
        return new DefaultKeyStoreSourceImpl(keyStore, oper, keyPassword);
    }
}
