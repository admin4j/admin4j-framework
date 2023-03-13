package com.admin4j.web.utils.compare;

import java.lang.annotation.*;

/**
 * @author andanyang
 * @since 2022/2/17 9:05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ObjectCompare {

    /**
     * true 比较所有字段。默认 没有ObjectCompareFiled注解的也会比较
     * false 比较ObjectCompareFiled 注解的字段
     *
     * @return
     */
    boolean allFile() default false;

}
