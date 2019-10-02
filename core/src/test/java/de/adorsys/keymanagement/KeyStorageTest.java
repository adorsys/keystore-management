package de.adorsys.keymanagement;

import de.adorsys.keymanagement.template.KeyPair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class KeyStorageTest {

    @Test
    void basicTest() {

        KeyStorageTemplate template = KeyStorageTemplate.builder()
                .withGeneratedKey(KeyPair.signing().alias("foo-bar").build())
                .withGeneratedKey(KeyPair.signing().build())
                .build();

        log.info("Arrr!");
    }
}