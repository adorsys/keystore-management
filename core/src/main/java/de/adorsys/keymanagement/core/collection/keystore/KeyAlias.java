package de.adorsys.keymanagement.core.collection.keystore;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import de.adorsys.keymanagement.core.WithAlias;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

@Getter
@RequiredArgsConstructor
public class KeyAlias implements WithAlias {

    public static final SimpleAttribute<KeyAlias, String> A_ID = attribute("alias", WithAlias::getAlias);

    private final String alias;
}
