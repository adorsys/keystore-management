package de.adorsys.keymanagement.core.view.entity;

import de.adorsys.keymanagement.core.collection.keystore.KeyMetadata;
import de.adorsys.keymanagement.core.template.NameAndPassword;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;

public class KeyEntry extends KeyAlias {

    private final ProvidedKeyEntry entry;

    public KeyEntry(String alias, KeyMetadata meta, ProvidedKeyEntry entry) {
        super(alias, meta);
        this.entry = entry;
    }

    public ProvidedKeyEntry asProvided() {
        return entry.toBuilder().keyTemplate(new NameAndPassword()).build();
    }
}
