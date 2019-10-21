package de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.keystore.KeyEntry;
import de.adorsys.keymanagement.bouncycastle.adapter.services.deprecated.types.keystore.KeyStoreType;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

public class KeystoreBuilder {
	private KeyStoreType storeType;
	private Map<String, KeyEntry> keyEntries = new HashMap<>();
	
	public KeystoreBuilder withStoreType(KeyStoreType storeType) {
		this.storeType = storeType;
		return this;
	}

	public KeystoreBuilder withKeyEntry(KeyEntry keyEntry) {
		this.keyEntries.put(keyEntry.getAlias(), keyEntry);
		return this;
	}

	public KeyStore build() {
		KeyStore ks = KeyStoreServiceImplBaseFunctions.newKeyStore(storeType);
		KeyStoreServiceImplBaseFunctions.fillKeyStore(ks, keyEntries.values());

		return ks;
	}
}
