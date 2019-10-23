package de.adorsys.keymanagement.api.types.template.provided;

import de.adorsys.keymanagement.api.types.entity.KeyPairEntry;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.template.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Delegate;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.List;
import java.util.function.Supplier;

@Getter
public class ProvidedKeyPair implements ProvidedKeyTemplate {

    @NonNull
    @Delegate
    private final KeyTemplate keyTemplate;

    @NonNull
    @Delegate
    private final KeyPair pair;

    @Getter
    private final KeyMetadata metadata;

    // Java keystore requires certificate (even self-signed) for private key.
    // If empty - will be generated.
    @NonNull
    private final List<Certificate> certificates;

    @Builder(builderClassName = "Templated", toBuilder = true)
    ProvidedKeyPair(@NonNull KeyTemplate keyTemplate, @NonNull KeyPair pair,
                    @Singular @NonNull List<Certificate> certificates, KeyMetadata metadata) {
        this.keyTemplate = keyTemplate;
        this.metadata = metadata;
        this.certificates = certificates;
        this.pair = pair;
    }

    @Builder(builderMethodName = "with")
    ProvidedKeyPair(String alias, String prefix, @NonNull Supplier<char[]> password, @NonNull KeyPair pair,
                    @Singular @NonNull List<Certificate> certificates, KeyMetadata metadata) {
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password);
        this.metadata = metadata;
        this.certificates = certificates;
        this.pair = pair;
    }

    public static ProvidedKeyPair from(KeyPairEntry entry, GeneratedKeyTemplate template) {

        return ProvidedKeyPair.builder()
                .keyTemplate(template)
                .pair(entry.getPair())
                .metadata(template.getMetadata())
                .certificates(entry.getCertificates())
                .build();
    }
}
