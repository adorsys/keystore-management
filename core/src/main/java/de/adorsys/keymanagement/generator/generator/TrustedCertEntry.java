package de.adorsys.keymanagement.generator.generator;

import de.adorsys.keymanagement.generator.deprecated.types.keystore.KeyEntry;
import org.bouncycastle.cert.X509CertificateHolder;

public interface TrustedCertEntry extends KeyEntry {
    X509CertificateHolder getCertificate();
}
