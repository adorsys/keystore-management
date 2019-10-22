package de.adorsys.keymanagement.core.source;

import de.adorsys.keymanagement.api.keystore.KeyStoreView;
import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.types.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.api.view.AliasView;
import de.adorsys.keymanagement.api.view.EntryView;
import de.adorsys.keymanagement.core.view.AliasViewImpl;
import de.adorsys.keymanagement.core.view.EntryViewImpl;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultKeyStoreView implements KeyStoreView {

    private final KeySource source;

    @Override
    public EntryView entries() {
        return new EntryViewImpl(source); // FIXME Inject/provide extra indexes
    }

    @Override
    public AliasView aliases() {
        return new AliasViewImpl(source); // FIXME Inject/provide extra indexes
    }

    @Override
    public KeySource source() {
        return source;
    }

    @Override
    public KeySet copyToKeySet(Function<String, char[]> keyPassword) {
        return KeySet.builder().keyEntries(
                entries().all().stream().map(
                        it -> ProvidedKeyEntry.with()
                                .alias(it.getAlias())
                                .entry(it.getEntry())
                                .password(() -> keyPassword.apply(it.getAlias()))
                                .build()
                ).collect(Collectors.toList())
        ).build();
    }

    @Override
    public KeySetTemplate copyToTemplate(Function<String, char[]> keyPassword) {
        return KeySetTemplate.builder().providedKeyEntries(
                entries().all().stream().map(
                        it -> ProvidedKeyEntry.with()
                                .alias(it.getAlias())
                                .entry(it.getEntry())
                                .password(() -> keyPassword.apply(it.getAlias()))
                                .build()
                ).collect(Collectors.toList())
        ).build();
    }
}
