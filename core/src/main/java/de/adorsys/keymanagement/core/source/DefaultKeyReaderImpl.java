package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.keystore.KeyStoreOper;
import de.adorsys.keymanagement.api.keystore.KeyStoreView;
import de.adorsys.keymanagement.api.metadata.KeyMetadataOper;
import de.adorsys.keymanagement.api.source.KeyReader;

import javax.inject.Inject;
import java.security.KeyStore;
import java.util.function.Function;

public class DefaultKeyReaderImpl implements KeyReader {

    private final KeyStoreOper oper;
    private final KeyMetadataOper metadataOper;

    @Inject
    public DefaultKeyReaderImpl(KeyStoreOper oper, KeyMetadataOper metadataOper) {
        this.oper = oper;
        this.metadataOper = metadataOper;
    }

    // FIXME must be dagger-configurable instead on using new
    @Override
    public KeyStoreView fromKeyStore(KeyStore keyStore, Function<String, char[]> keyPassword) {
        return new DefaultKeyStoreView(
                new DefaultKeyStoreSourceImpl(metadataOper, keyStore, oper, keyPassword)
        );
    }
}
