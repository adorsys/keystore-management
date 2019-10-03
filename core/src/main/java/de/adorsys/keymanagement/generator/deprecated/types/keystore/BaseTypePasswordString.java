package de.adorsys.keymanagement.generator.deprecated.types.keystore;

import lombok.Getter;
import lombok.Synchronized;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import java.util.Arrays;

/**
 * Wrapper for password sensitive data.
 */
@Getter
public class BaseTypePasswordString implements Destroyable {

    private final char[] value;
    private boolean destroyed;

    public BaseTypePasswordString(String value) {
        this.value = Arrays.copyOf(value.toCharArray(), value.length());
    }

    public BaseTypePasswordString(char[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }

    @Override
    @Synchronized
    public void destroy() throws DestroyFailedException {
        Arrays.fill(value, ' ');
        destroyed = true;
    }

    @Synchronized
    public boolean isDestroyed() {
        return destroyed;
    }
}
