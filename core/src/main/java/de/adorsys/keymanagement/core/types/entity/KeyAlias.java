package de.adorsys.keymanagement.core.types.entity;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import de.adorsys.keymanagement.core.types.entity.metadata.KeyMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.googlecode.cqengine.query.QueryFactory.attribute;
import static com.googlecode.cqengine.query.QueryFactory.nullableAttribute;

@Getter
@RequiredArgsConstructor
public class KeyAlias {

    public static final SimpleAttribute<KeyAlias, String> A_ID = attribute("alias", KeyAlias::getAlias);
    public static final SimpleNullableAttribute<KeyAlias, KeyMetadata> META = nullableAttribute("meta", KeyAlias::getMeta);

    private final String alias;
    private final KeyMetadata meta;
}
