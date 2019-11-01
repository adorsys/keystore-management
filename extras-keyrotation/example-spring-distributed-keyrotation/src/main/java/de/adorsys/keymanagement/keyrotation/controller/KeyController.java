package de.adorsys.keymanagement.keyrotation.controller;

import com.google.common.collect.Iterables;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.keyrotation.service.JWKExporter;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static de.adorsys.keymanagement.keyrotation.api.types.CommonValidities.DEFAULT_FILTER;

@RestController
@RequestMapping("/keys")
@RequiredArgsConstructor
public class KeyController {

    private final RotatedKeyStore rotatedKeyStore;
    private final JWKExporter exporter;

    @GetMapping("/{keyId}")
    public ResponseEntity<String> key(@PathVariable("keyId") String keyId) {
        return ResponseEntity.ok(exporter.export(keyId).toJSONString());
    }

    @GetMapping("/random/secret")
    public ResponseEntity<String> randomSecret() {
        KeyEntry randomSecret = Iterables.getFirst(
                rotatedKeyStore.keys().withValidity(DEFAULT_FILTER).secretKeys().pickNrandom(1),
                null
        );

        return ResponseEntity.ok(exporter.export(randomSecret.getAlias()).toJSONString());
    }
}
