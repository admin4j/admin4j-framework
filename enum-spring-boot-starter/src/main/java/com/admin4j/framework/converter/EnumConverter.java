package com.admin4j.framework.converter;

import org.springframework.lang.Nullable;

/**
 * 枚举转化器
 *
 * @author andanyang
 * @since 2023/11/1 9:57
 */
public interface EnumConverter {


    @Nullable
    <T extends Enum> T convert(String source, Class<T> enumType);
}
