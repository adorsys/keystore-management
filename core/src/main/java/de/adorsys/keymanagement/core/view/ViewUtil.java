package de.adorsys.keymanagement.core.view;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Member;
import java.util.function.Function;

@UtilityClass
class ViewUtil {

    static final Function<Member, String> PROP_NAME = it -> it.getName().replaceAll("^get", "").toLowerCase();
}
