package de.adorsys.keymanagement.api;

public interface KeyStorage {

    AliasCollection aliases();
    KeyCollection keys();
}
