package de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore.KeyEntry;
import org.bouncycastle.cert.X509CertificateHolder;

public interface TrustedCertEntry extends KeyEntry {
    X509CertificateHolder getCertificate();
}
