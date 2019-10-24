package de.adorsys.keymanagement.api.keystore;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.view.AliasView;
import de.adorsys.keymanagement.api.view.EntryView;

import java.util.function.Function;

public interface KeyStoreView {

    /**
     * Returns keystore entries EXCLUDING metadata entries.
     */
    <Q> EntryView<Q> entries();

    /**
     * Returns keystore entries EXCLUDING metadata entries.
     */
    <Q> AliasView<Q> aliases();

    /**
     * Returns all keystore entries including metadata entries.
     */
    <Q> EntryView<Q> allEntries();

    /**
     * Returns all keystore entries including metadata entries.
     */
    <Q> AliasView<Q> allAliases();

    KeySource source();
    KeySet copyToKeySet(Function<String, char[]> keyPassword);
    KeySetTemplate copyToTemplate(Function<String, char[]> keyPassword);
}
