package de.adorsys.keymanagement.generator.generator;

import de.adorsys.keymanagement.generator.deprecated.types.keystore.KeyEntry;
import de.adorsys.keymanagement.generator.deprecated.types.keystore.ReadKeyPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class KeyEntryData implements KeyEntry {

	private final ReadKeyPassword readKeyPassword;
	
	private final String alias;
}
