package de.adorsys.keymanagement.keyrotation.api;

public interface RotationLocker {

    void executeWithLock(Runnable runnable);
}
