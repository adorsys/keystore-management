package de.adorsys.keymanagement.keyrotation.controller;

import com.google.common.collect.Iterables;
import com.nimbusds.jose.jwk.JWK;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.keyrotation.service.JWKExporter;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static de.adorsys.keymanagement.keyrotation.api.types.CommonValidities.DEFAULT_VALIDITY;

@RestController
@RequestMapping("/keys")
@RequiredArgsConstructor
@Tag(name = "keys")
public class KeyController {

    private final RotatedKeyStore rotatedKeyStore;
    private final JWKExporter exporter;

    @GetMapping("/{keyId}")
    @Operation(summary = "Get key by id")
    public ResponseEntity<String> key(@PathVariable("keyId") String keyId) {
        JWK key = exporter.export(keyId);
        if (null == key) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(key.toJSONString());
    }

    @GetMapping("/random/secret")
    @Operation(summary = "Get random valid secret key")
    public ResponseEntity<String> randomSecret() {
        KeyEntry randomSecret = Iterables.getFirst(
                rotatedKeyStore.keys().withValidity(DEFAULT_VALIDITY).secretKeys().pickNrandom(1),
                null
        );

        JWK key = exporter.export(randomSecret.getAlias());
        if (null == key) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(key.toJSONString());
    }
}
