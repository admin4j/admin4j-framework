package com.admin4j.framework.autoconfigure;

import com.admin4j.framework.converter.EnumConverter;
import com.admin4j.framework.converter.factory.EnumConverterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/11/1 13:26
 */
@RequiredArgsConstructor
public class EnumConverterWebConfigure implements WebMvcConfigurer {

    private final List<EnumConverter> enumConverters;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverterFactory(enumConverters));
    }
}
