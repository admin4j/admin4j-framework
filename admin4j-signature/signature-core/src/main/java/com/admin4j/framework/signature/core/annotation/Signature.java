package com.admin4j.framework.signature.core.annotation;

import com.admin4j.framework.signature.core.DefaultSignatureStrategy;
import com.admin4j.framework.signature.core.SignatureStrategy;

import java.lang.annotation.*;


/**
 * 用于标记接口签名
 *
 * @author zhougang
 * @since 2022/3/24 16:34
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Signature {

    /**
     * 是否开启接口签名
     */
    boolean enable() default true;

    Class<? extends SignatureStrategy> signatureClass() default DefaultSignatureStrategy.class;

    /**
     * 签名字段
     */
    SignatureField appId() default @SignatureField(filedName = "appId");

    SignatureField timestamp() default @SignatureField(filedName = "timestamp");

    SignatureField nonce() default @SignatureField(filedName = "nonce");

    SignatureField sign() default @SignatureField(filedName = "sign");

    SignatureField uri() default @SignatureField(filedName = "uri");
}
