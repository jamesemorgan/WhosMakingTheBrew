package com.morgan.design.utils;

import java.util.List;

/**
 * @deprecated can we replace with with Guava?
 */
@Deprecated
public final class ObjectUtils {

    public static boolean isNull(final Object value) {
        return null == value;
    }

    public static boolean isNotNull(final Object value) {
        return !isNull(value);
    }

    public static boolean isEmpty(final List<?> value) {
        return !isNull(value) && value.isEmpty();
    }

    public static boolean isNotEmpty(final List<?> value) {
        return !isEmpty(value);
    }

    public static boolean isZero(final int value) {
        return 0 == value;
    }

    public static boolean isZero(final long value) {
        return value == 0L;
    }

}
