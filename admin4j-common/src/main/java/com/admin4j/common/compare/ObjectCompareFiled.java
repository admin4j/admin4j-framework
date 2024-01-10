package com.admin4j.common.compare;

import java.lang.annotation.*;

/**
 * @author andanyang
 * @since 2022/2/17 9:06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ObjectCompareFiled {

    /**
     * 忽略比较
     *
     * @return
     */
    boolean ignore() default false;

    /**
     * 目标为空时，是否忽略比较
     *
     * @return
     */
    boolean ignoreTargetNull() default false;

    String value() default "";
}
