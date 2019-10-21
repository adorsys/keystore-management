package de.adorsys.keymanagement.deprecated.generator;

import lombok.experimental.UtilityClass;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

@UtilityClass
public class ProviderUtils {

    public static final Provider bcProvider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
}
