package de.adorsys.keymanagement.keyrotation.controller;

import com.nimbusds.jose.jwk.JWKSet;
import de.adorsys.keymanagement.api.types.entity.KeyAlias;
import de.adorsys.keymanagement.keyrotation.api.services.KeyViewWithValidity;
import de.adorsys.keymanagement.keyrotation.service.JWKExporter;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static de.adorsys.keymanagement.keyrotation.api.types.CommonValidities.DEFAULT_FILTER;

@RestController
@RequestMapping("/pop")
@RequiredArgsConstructor
public class PopController {

    private final JWKExporter exporter;
    private final RotatedKeyStore rotatedKeyStore;

    @GetMapping
    public ResponseEntity<String> pop() {
        KeyViewWithValidity view = rotatedKeyStore.keys().withValidity(DEFAULT_FILTER);
        return ResponseEntity.ok(
                new JWKSet(new ArrayList<>(
                exporter.export(
                        view.all().stream().map(KeyAlias::getAlias).collect(Collectors.toSet())))
                ).toJSONObject().toJSONString()
        );
    }
}
