package de.adorsys.keymanagement.api.types.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.List;

@Getter
@Builder
public class KeyPairEntry {

    @NonNull
    private final KeyPair pair;

    @NonNull
    @Singular
    private final List<Certificate> certificates;
}
