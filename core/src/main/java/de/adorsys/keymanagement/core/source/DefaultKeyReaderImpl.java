package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.KeyReader;
import de.adorsys.keymanagement.api.KeyStoreOper;
import de.adorsys.keymanagement.api.KeyStoreView;

import javax.inject.Inject;
import java.security.KeyStore;
import java.util.function.Function;

public class DefaultKeyReaderImpl implements KeyReader {

    private final KeyStoreOper oper;

    @Inject
    public DefaultKeyReaderImpl(KeyStoreOper oper) {
        this.oper = oper;
    }

    // FIXME must be dagger-configurable instead on using new
    @Override
    public KeyStoreView fromKeyStore(KeyStore keyStore, Function<String, char[]> keyPassword) {
        return new DefaultKeyStoreView(
                new DefaultKeyStoreSourceImpl(keyStore, oper, keyPassword)
        );
    }
}
