package de.adorsys.keymanagement.deprecated.generator;

import de.adorsys.keymanagement.deprecated.types.keystore.KeyEntry;
import org.bouncycastle.cert.X509CertificateHolder;

public interface TrustedCertEntry extends KeyEntry {
    X509CertificateHolder getCertificate();
}
