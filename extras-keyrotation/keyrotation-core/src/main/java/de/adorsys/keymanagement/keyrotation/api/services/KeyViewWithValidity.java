package de.adorsys.keymanagement.keyrotation.api.services;

import de.adorsys.keymanagement.api.types.ResultCollection;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;

public interface KeyViewWithValidity {

    ResultCollection<KeyEntry> all();
    ResultCollection<KeyEntry> encryptionKeys();
    ResultCollection<KeyEntry> signingKeys();
    ResultCollection<KeyEntry> secretKeys();
}
