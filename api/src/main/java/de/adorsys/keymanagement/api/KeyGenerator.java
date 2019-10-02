package de.adorsys.keymanagement.api;

import java.util.stream.Stream;

public interface KeyGenerator {

    Stream<Key> generate();
}
