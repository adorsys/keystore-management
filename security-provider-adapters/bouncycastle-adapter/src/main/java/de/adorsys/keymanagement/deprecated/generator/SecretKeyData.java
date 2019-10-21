package de.adorsys.keymanagement.deprecated.generator;

import de.adorsys.keymanagement.deprecated.types.keystore.ReadKeyPassword;
import de.adorsys.keymanagement.deprecated.types.keystore.SecretKeyEntry;
import lombok.Builder;
import lombok.Getter;

import javax.crypto.SecretKey;

@Getter
public class SecretKeyData extends KeyEntryData implements SecretKeyEntry {

	private final SecretKey secretKey;
	private final String keyAlgo;

	@Builder
	private SecretKeyData(ReadKeyPassword readKeyPassword, String alias, SecretKey secretKey, String keyAlgo) {
		super(readKeyPassword, alias);
		this.secretKey = secretKey;
		this.keyAlgo = keyAlgo;
	}
}
