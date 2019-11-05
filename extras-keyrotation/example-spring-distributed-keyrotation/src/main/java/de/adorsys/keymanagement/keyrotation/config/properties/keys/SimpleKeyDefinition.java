package de.adorsys.keymanagement.keyrotation.config.properties.keys;

import lombok.Data;

@Data
public class SimpleKeyDefinition {

    private String algo;
    private int size;
    private String sigAlgo;
}
