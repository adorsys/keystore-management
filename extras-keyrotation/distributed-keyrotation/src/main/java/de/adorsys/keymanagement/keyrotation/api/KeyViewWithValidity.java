package de.adorsys.keymanagement.keyrotation.api;

import de.adorsys.keymanagement.api.types.ResultCollection;

import java.security.Key;

public interface KeyViewWithValidity {

    ResultCollection<Key> encryptionKeys();
    ResultCollection<Key> signingKeys();
    ResultCollection<Key> secretKeys();
}
