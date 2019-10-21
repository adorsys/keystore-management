package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.source.KeyReader;
import de.adorsys.keymanagement.api.keystore.KeyStoreView;
import de.adorsys.keymanagement.api.keystore.KeyStoreOper;

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
