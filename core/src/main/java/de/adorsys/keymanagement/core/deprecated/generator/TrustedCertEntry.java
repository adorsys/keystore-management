package de.adorsys.keymanagement.core.deprecated.generator;

import de.adorsys.keymanagement.core.deprecated.types.keystore.KeyEntry;
import org.bouncycastle.cert.X509CertificateHolder;

public interface TrustedCertEntry extends KeyEntry {
    X509CertificateHolder getCertificate();
}
