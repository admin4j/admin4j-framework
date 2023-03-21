package com.admin4j.framework.web.utils;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author andanyang
 * @since 2022/3/10 17:42
 */
public class TargetType<T> {

    private Type type;
    @Getter
    private Class<T> classType;

    @SuppressWarnings("unchecked")
    public TargetType() {
        Type superClass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        if (this.type instanceof ParameterizedType) {
            this.classType = (Class<T>) ((ParameterizedType) this.type).getRawType();
        } else {
            this.classType = (Class<T>) this.type;
        }
    }
}
