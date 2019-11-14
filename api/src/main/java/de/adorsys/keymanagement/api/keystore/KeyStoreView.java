package de.adorsys.keymanagement.api.keystore;

import de.adorsys.keymanagement.api.source.KeySource;
import de.adorsys.keymanagement.api.types.KeySetTemplate;
import de.adorsys.keymanagement.api.types.source.KeySet;
import de.adorsys.keymanagement.api.view.AliasView;
import de.adorsys.keymanagement.api.view.EntryView;

import java.util.function.Function;

/**
 * {@link java.security.KeyStore} view from different angles.
 * IMPORTANT: each opened view (i.e. entries() or metadata()) is independent, so while it will propagate
 * changes done through it into keystore, it will not see changes done from another view.
 */
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

    /**
     * Get source of keys. For example KeySource can wrap following entities - java Keystore, Database table,
     * list of Key instances, etc.
     * @return object which have access to keys and can operate with them
     */
    KeySource source();

    /**
     * Clones current KeySource into KeySet in a way, that all keys will be materialized in KeySet.
     * You can view it as generates keySet from all existing in key source keys
     * @param keyPassword password which is used to access keys
     * @return keySet with lists of key pairs, secret keys or key entries
     */
    KeySet copyToKeySet(Function<String, char[]> keyPassword);

    /**
     * Clones current KeySource into KeySetTemplate in a way, that all keys will be materialized in KeySetTemplate.
     * You can view it as generating template from all existing in key source keys
     * @param keyPassword password which is used to access keys
     * @return key set template with lists of key pairs, secret keys or key entries
     */
    KeySetTemplate copyToTemplate(Function<String, char[]> keyPassword);
}
