package com.admin4j.framework.web.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author andanyang
 * @since 2024/3/27 18:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = TimeZoneFormatSerializer.class)
public @interface TimeZoneFormat {

    /**
     * 时间单位，默认毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 时间格式
     *
     * @return
     */
    String dateFormatter() default "yyyy-MM-dd HH:mm:ss";
}
