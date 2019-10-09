package de.adorsys.keymanagement.core.collection.keyset;

import com.googlecode.cqengine.attribute.Attribute;
import de.adorsys.keymanagement.api.Queryable;
import de.adorsys.keymanagement.core.template.KeyTemplate;
import de.adorsys.keymanagement.core.template.provided.Provided;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyEntry;
import de.adorsys.keymanagement.core.template.provided.ProvidedKeyPair;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

@Getter
@EqualsAndHashCode(of = "alias")
public class QueryableProvided implements Queryable {

    public static final Attribute<QueryableProvided, String> ID = attribute("id", QueryableProvided::getAlias);
    public static final Attribute<QueryableProvided, Boolean> IS_SECRET = attribute(
            "is_secret",
            it -> (null != it.getKey() && it.getKey().getKey() instanceof SecretKey)
                    || (null != it.getEntry() && it.getEntry().getEntry() instanceof KeyStore.SecretKeyEntry)
    );

    public static final Attribute<QueryableProvided, Boolean> IS_PRIVATE = attribute(
            "is_private",
            it -> (null != it.getKey() && it.getKey().getKey() instanceof PrivateKey)
                    || (null != it.getEntry() && it.getEntry().getEntry() instanceof KeyStore.PrivateKeyEntry)
                    || (null != it.getPair())
    );

    private final String alias;
    private final ProvidedKeyEntry entry;
    private final Provided key;
    private final ProvidedKeyPair pair;

    @Builder(toBuilder = true)
    public QueryableProvided(ProvidedKeyEntry entry, Provided key, ProvidedKeyPair pair) {
        this.entry = entry;
        this.key = key;
        this.pair = pair;

        val validate = Stream.of(entry, key, pair);
        List<KeyTemplate> nonNull = validate.filter(Objects::nonNull).collect(Collectors.toList());

        if (nonNull.isEmpty()) {
            throw new IllegalArgumentException("Expecting to have either: entry or key or pair");
        }

        if (1 != nonNull.size()) {
            throw new IllegalArgumentException("Expecting to have only one of: entry or key or pair");
        }

        this.alias = nonNull.get(0).generateName();
    }
}
