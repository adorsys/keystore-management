package de.adorsys.keymanagement.collection;

import com.googlecode.cqengine.attribute.SimpleAttribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

@Getter
@RequiredArgsConstructor
public class WithAlias {

    public static final SimpleAttribute<WithAlias, String> A_ID = attribute("alias", WithAlias::getAlias);

    private final String alias;
}
