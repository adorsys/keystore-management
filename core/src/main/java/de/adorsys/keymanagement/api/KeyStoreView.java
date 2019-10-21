package de.adorsys.keymanagement.api;

import de.adorsys.keymanagement.core.types.KeySetTemplate;
import de.adorsys.keymanagement.core.types.source.KeySet;
import de.adorsys.keymanagement.core.view.AliasView;
import de.adorsys.keymanagement.core.view.EntryView;

import java.util.function.Function;

public interface KeyStoreView {

    EntryView entries();
    AliasView aliases();
    KeySource source();
    KeySet copyToKeySet(Function<String, char[]> keyPassword);
    KeySetTemplate copyToTemplate(Function<String, char[]> keyPassword);
}
