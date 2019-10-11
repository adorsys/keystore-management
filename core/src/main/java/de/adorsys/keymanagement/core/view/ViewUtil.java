package de.adorsys.keymanagement.core.view;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Member;
import java.util.function.Function;

@UtilityClass
class ViewUtil {

    static final Function<Member, String> SNAKE_CASE = it -> it.getName()
            .replaceAll("^get", "")
            .replaceAll("([A-Z])", "_\\$1") // to snake-case
            .toLowerCase();
}
