package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.keystore.KeyStoreOper;
import de.adorsys.keymanagement.api.keystore.KeyStoreView;
import de.adorsys.keymanagement.api.persist.KeyMetadataPersister;
import de.adorsys.keymanagement.api.source.KeyMetadataExtractor;
import de.adorsys.keymanagement.api.source.KeyReader;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.security.KeyStore;
import java.util.function.Function;

public class DefaultKeyReaderImpl implements KeyReader {

    private final KeyStoreOper oper;
    private final KeyMetadataExtractor extractor;
    private final KeyMetadataPersister persister;

    @Inject
    public DefaultKeyReaderImpl(
            KeyStoreOper oper, @Nullable KeyMetadataExtractor extractor, @Nullable KeyMetadataPersister persister) {
        this.oper = oper;
        this.extractor = extractor;
        this.persister = persister;
    }

    // FIXME must be dagger-configurable instead on using new
    @Override
    public KeyStoreView fromKeyStore(KeyStore keyStore, Function<String, char[]> keyPassword) {
        return new DefaultKeyStoreView(
                new DefaultKeyStoreSourceImpl(extractor, persister, keyStore, oper, keyPassword)
        );
    }
}
