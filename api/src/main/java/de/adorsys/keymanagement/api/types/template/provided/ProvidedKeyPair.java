package de.adorsys.keymanagement.api.types.template.provided;

import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import de.adorsys.keymanagement.api.types.template.ProvidedKeyTemplate;
import de.adorsys.keymanagement.api.types.template.DefaultNamingStrategy;
import de.adorsys.keymanagement.api.types.template.KeyTemplate;
import de.adorsys.keymanagement.api.types.template.NameAndPassword;
import lombok.*;
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

    // Java keystore requires certificate (even self-signed) for private key.
    // If empty - will be generated.
    @NonNull
    private final List<Certificate> certificates;

    @Builder(builderClassName = "Templated", toBuilder = true)
    ProvidedKeyPair(@NonNull KeyTemplate keyTemplate, @NonNull KeyPair pair, @Singular @NonNull List<Certificate> certificates) {
        this.keyTemplate = keyTemplate;
        this.certificates = certificates;
        this.pair = pair;
    }

    @Builder(builderMethodName = "with")
    ProvidedKeyPair(String alias, String prefix, @NonNull Supplier<char[]> password, @NonNull KeyPair pair,
                    @Singular @NonNull List<Certificate> certificates, KeyMetadata metadata) {
        this.keyTemplate = new NameAndPassword(new DefaultNamingStrategy(alias, prefix), password, metadata);
        this.certificates = certificates;
        this.pair = pair;
    }
}
