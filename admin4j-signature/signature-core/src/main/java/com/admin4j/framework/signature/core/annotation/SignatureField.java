package com.admin4j.framework.signature.core.annotation;

import java.lang.annotation.*;

/**
 * 签名算法实现 => 指定哪些字段需要进行签名
 */
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SignatureField {

    /**
     * 字段：自定义字段名，对应前端传入的字段名
     */
    String filedName() default "";

    /**
     * 是否参与签名
     */
    boolean enable() default true;

}