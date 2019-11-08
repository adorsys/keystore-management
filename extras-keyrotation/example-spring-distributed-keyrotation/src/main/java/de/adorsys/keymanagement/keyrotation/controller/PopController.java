package de.adorsys.keymanagement.keyrotation.controller;

import com.nimbusds.jose.jwk.JWKSet;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;
import de.adorsys.keymanagement.keyrotation.api.services.KeyViewWithValidity;
import de.adorsys.keymanagement.keyrotation.service.JWKExporter;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static de.adorsys.keymanagement.keyrotation.api.types.CommonValidities.DEFAULT_VALIDITY;

@RestController
@RequestMapping("/pop")
@RequiredArgsConstructor
@Tag(name = "pop-keys")
public class PopController {

    private final JWKExporter exporter;
    private final RotatedKeyStore rotatedKeyStore;

    @GetMapping
    @Operation(summary = "Get Proof-of-Possession valid public keys to encrypt with")
    public ResponseEntity<String> pop() {
        KeyViewWithValidity view = rotatedKeyStore.keys().withValidity(DEFAULT_VALIDITY);

        return ResponseEntity.ok(
                new JWKSet(new ArrayList<>(
                exporter.export(
                        view.encryptionKeys().stream().map(KeyAlias::getAlias).collect(Collectors.toSet())))
                ).toPublicJWKSet().toJSONObject().toJSONString()
        );
    }
}
