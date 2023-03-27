package com.admin4j.framework.security.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;


/**
 * 用于标记匿名访问方法
 *
 * @author andanyang
 * @since 2022/3/24 16:34
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {

    RequestMethod[] method() default {};
}
