package com.admin4j.framework.converter.impl;


import com.admin4j.framework.converter.EnumConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 使用 EnumValue注解 字段进行匹配
 *
 * @author andanyang
 * @since 2023/11/1 10:07
 */
// @Service
public class EnumValueConverter<A extends Annotation> implements EnumConverter {

    private final Class<A> targetAnnotation;

    public EnumValueConverter(Class<A> targetAnnotation) {
        this.targetAnnotation = targetAnnotation;
    }

    @Override
    public <T extends Enum> T convert(String source, Class<T> enumType) {

        // 被注解的值字段
        Field valueField = null;
        Field[] declaredFields = enumType.getDeclaredFields();
        for (Field field : declaredFields) {
            Annotation annotation = field.getAnnotation(targetAnnotation);
            if (annotation != null) {
                valueField = field;
            }
        }

        if (valueField != null) {
            T[] enumConstants = enumType.getEnumConstants();
            // 查找枚举
            for (T e : enumConstants) {
                try {
                    valueField.setAccessible(true);
                    if (String.valueOf(valueField.get(e)).equals(source)) {
                        return e;
                    }
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return null;
    }
}
