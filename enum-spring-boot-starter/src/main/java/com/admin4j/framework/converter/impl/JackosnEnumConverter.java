package com.admin4j.framework.converter.impl;


import com.admin4j.framework.converter.EnumConverter;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 使用   @JsonCreator 方法注解静态字段进行匹配
 *
 * @author andanyang
 * @since 2023/11/1 10:07
 */

@RequiredArgsConstructor
public class JackosnEnumConverter implements EnumConverter {

    private final ObjectProvider<ConversionService> conversionServiceObjectProvider;

    @Override
    public <T extends Enum> T convert(String source, Class<T> enumType) {

        Method[] declaredMethods = enumType.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(JsonCreator.class)) {
                try {
                    Object invoke;
                    Class<?> parameterType = method.getParameterTypes()[0];
                    Object cast = conversionServiceObjectProvider.getIfAvailable().convert(source, parameterType);
                    invoke = method.invoke(enumType, cast);

                    if (invoke == null) {
                        return null;
                    }
                    if (!enumType.isInstance(invoke)) {
                        throw new IllegalArgumentException("JsonCreator error: target " + enumType.getCanonicalName() + " but get " + invoke.getClass().getCanonicalName());
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }
}
