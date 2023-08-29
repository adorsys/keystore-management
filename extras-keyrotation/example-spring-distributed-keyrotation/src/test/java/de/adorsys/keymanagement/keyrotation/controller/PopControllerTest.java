package de.adorsys.keymanagement.keyrotation.controller;

import com.nimbusds.jose.jwk.JWKSet;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test validates basic REST functionality. It should use default profile.
 */
class PopControllerTest extends BaseEndpointTest {

    @Autowired
    protected TestRestTemplate rest;

    @Autowired
    private RotatedKeyStore keyStore;

    @Test
    @SneakyThrows
    void pop() {
        keyStore.forRotation().rotate();

        assertThat(JWKSet.parse(rest.getForObject("/pop", String.class)).getKeys()).hasSize(3);
    }
}