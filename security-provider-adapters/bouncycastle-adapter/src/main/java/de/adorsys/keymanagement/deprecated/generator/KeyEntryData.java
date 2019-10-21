package de.adorsys.keymanagement.deprecated.generator;

import de.adorsys.keymanagement.deprecated.types.keystore.KeyEntry;
import de.adorsys.keymanagement.deprecated.types.keystore.ReadKeyPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class KeyEntryData implements KeyEntry {

	private final ReadKeyPassword readKeyPassword;
	
	private final String alias;
}
