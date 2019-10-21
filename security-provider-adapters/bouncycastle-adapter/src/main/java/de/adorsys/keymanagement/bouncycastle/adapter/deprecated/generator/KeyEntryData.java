package de.adorsys.keymanagement.bouncycastle.adapter.deprecated.generator;

import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore.KeyEntry;
import de.adorsys.keymanagement.bouncycastle.adapter.deprecated.types.keystore.ReadKeyPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class KeyEntryData implements KeyEntry {

	private final ReadKeyPassword readKeyPassword;
	
	private final String alias;
}
