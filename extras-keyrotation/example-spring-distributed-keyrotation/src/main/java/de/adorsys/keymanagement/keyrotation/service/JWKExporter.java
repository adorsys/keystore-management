package de.adorsys.keymanagement.keyrotation.service;

import com.nimbusds.jose.jwk.JWK;
import de.adorsys.keymanagement.keyrotation.config.properties.RotationConfiguration;
import de.adorsys.keymanagement.keyrotation.services.RotatedKeyStore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JWKExporter {

    private final RotationConfiguration config;
    private final RotatedKeyStore rotatedKeyStore;

    @SneakyThrows
    public JWK export(String keyId) {
        KeyStore ks = rotatedKeyStore.keys().keyStore();
        return readKey(keyId, ks);
    }

    @SneakyThrows
    public Set<JWK> export(Set<String> keyIds) {
        KeyStore ks = rotatedKeyStore.keys().keyStore();
        return keyIds.stream().map(it -> readKey(it, ks)).collect(Collectors.toSet());
    }

    @SneakyThrows
    private JWK readKey(String alias, KeyStore ks) {
        return JWK.load(ks, alias, config.getKeystore().getPassword().toCharArray());
    }
}
