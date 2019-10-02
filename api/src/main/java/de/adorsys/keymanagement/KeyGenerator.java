package de.adorsys.keymanagement;

import java.util.stream.Stream;

public interface KeyGenerator {

    Stream<Key> generate();
}
