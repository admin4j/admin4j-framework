package com.admin4j.framework.signature.annotation;

import com.admin4j.framework.signature.DefaultSignature;
import com.admin4j.framework.signature.SignatureService;

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

    Class<? extends SignatureService> signatureClass() default DefaultSignature.class;

    /**
     * 签名字段
     *
     * @return
     */
    SignatureField appId() default @SignatureField(filedName = "appId", order = 0);

    SignatureField timestamp() default @SignatureField(filedName = "timestamp", order = 1);

    SignatureField nonce() default @SignatureField(filedName = "nonce", order = 2);

    SignatureField sign() default @SignatureField(filedName = "sign", order = 3);
}
