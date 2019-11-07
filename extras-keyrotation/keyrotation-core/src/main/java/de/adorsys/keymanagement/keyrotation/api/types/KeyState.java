package de.adorsys.keymanagement.keyrotation.api.types;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.support.SimpleFunction;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.QueryFactory;
import de.adorsys.keymanagement.api.types.entity.KeyEntry;
import de.adorsys.keymanagement.api.types.entity.metadata.KeyMetadata;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.function.Function;

import static com.googlecode.cqengine.query.QueryFactory.attribute;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.lessThan;

@Getter
@Builder(toBuilder = true)
public class KeyState implements KeyMetadata {

    public static final SimpleFunction<KeyEntry, KeyState> STATE = it -> it.getMeta(KeyState.class);
    public static final Attribute<KeyEntry, KeyStatus> STATUS = attribute(it -> STATE.apply(it).getStatus());
    public static final Attribute<KeyEntry, KeyType> TYPE = attribute(it -> STATE.apply(it).getType());
    public static final Attribute<KeyEntry, Instant> NOT_AFTER = attribute(it -> STATE.apply(it).getNotAfter());
    public static final Attribute<KeyEntry, Instant> EXPIRE_AT = attribute(it -> STATE.apply(it).getExpireAt());

    public static final Function<Instant, Query<KeyEntry>> BECAME_LEGACY = now -> QueryFactory.and(
            equal(STATUS, KeyStatus.VALID),
            lessThan(NOT_AFTER, now)
    );

    public static final Function<Instant, Query<KeyEntry>> BECAME_EXPIRED = now -> QueryFactory.and(
            equal(STATUS, KeyStatus.LEGACY),
            lessThan(EXPIRE_AT, now)
    );

    private final KeyStatus status;
    private final KeyType type;

    /**
     * UTC instant of when entry was created.
     */
    private final Instant createdAt;

    /**
     * UTC instant of time after which key should become `Legacy`.
     */
    private final Instant notAfter;

    /**
     * UTC instant of time after which key should be removed.
     */
    private final Instant expireAt;
}
