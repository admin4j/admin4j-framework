package com.admin4j.framework.web.utils.compare;

import java.lang.annotation.*;

/**
 * @author andanyang
 * @since 2022/2/17 9:06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ObjectCompareFiled {

    boolean ignore() default false;

    String value() default "";
}
