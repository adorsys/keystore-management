package de.adorsys.keymanagement.keyrotation.api.persistence;

public interface RotationLocker {

    void executeWithLock(Runnable runnable);
    void clearCache();
}
