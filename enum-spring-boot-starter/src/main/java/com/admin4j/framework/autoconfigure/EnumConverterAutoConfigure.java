package com.admin4j.framework.autoconfigure;

import com.admin4j.framework.converter.EnumConverter;
import com.admin4j.framework.converter.impl.EnumValueConverter;
import com.admin4j.framework.converter.impl.JackosnEnumConverter;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/11/1 11:37
 */
public class EnumConverterAutoConfigure {

    @Bean
    @Order(1)
    @ConditionalOnClass(name = "com.baomidou.mybatisplus.annotation.EnumValue")
    public EnumValueConverter enumValueConverter() {
        return new EnumValueConverter(EnumValue.class);
    }

    @Bean
    @Order(2)
    @ConditionalOnClass(name = "com.fasterxml.jackson.annotation.JsonValue")
    public EnumValueConverter jsonEnumValueConverter() {
        return new EnumValueConverter(JsonValue.class);
    }

    @Bean
    @Order(3)
    @ConditionalOnClass(name = "com.fasterxml.jackson.annotation.JsonCreator")
    public JackosnEnumConverter jackosnEnumConverter(ObjectProvider<ConversionService> objectProvider) {
        return new JackosnEnumConverter(objectProvider);
    }

    @Bean
    @ConditionalOnBean(EnumConverter.class)
    public EnumConverterWebConfigure enumConverterWebConfigure(List<EnumConverter> enumConverters) {
        return new EnumConverterWebConfigure(enumConverters);
    }
}
