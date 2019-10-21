package de.adorsys.keymanagement.api.keystore;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.view.AliasView;
import de.adorsys.keymanagement.api.view.EntryView;

import java.util.function.Function;

public interface KeyStoreView<ALIAS, ENTRY> {

    EntryView<ENTRY> entries();
    AliasView<ALIAS> aliases();
    KeySource source();
    KeySet copyToKeySet(Function<String, char[]> keyPassword);
    KeySetTemplate copyToTemplate(Function<String, char[]> keyPassword);
}
