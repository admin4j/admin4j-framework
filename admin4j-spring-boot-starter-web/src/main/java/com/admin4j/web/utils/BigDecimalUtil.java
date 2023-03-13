package com.admin4j.web.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author andanyang
 * @since 2022/4/11 15:40
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BigDecimalUtil {

    //-1
    public static final BigDecimal NEGATIVE_ONE = new BigDecimal("-1");
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public static boolean isEmpty(BigDecimal bigDecimal) {

        return bigDecimal == null || BigDecimal.ZERO.compareTo(bigDecimal) == 0;
    }

    public static final BigDecimal ONE_HUNDREDTH = new BigDecimal("0.01");
}
