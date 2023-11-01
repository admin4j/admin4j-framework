package com.admin4j.framework.converter.factory;


import com.admin4j.framework.converter.EnumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andanyang
 * @since 2023/11/1 9:22
 */
@RequiredArgsConstructor
public class EnumConverterFactory implements ConverterFactory<String, Enum> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Converter> CONVERTERS = new ConcurrentHashMap<>(64);
    private final List<EnumConverter> enumConverters;

    /**
     * Get the converter to convert from S to target type T, where T is also an instance of R.
     *
     * @param targetType the target type to convert to
     * @return a converter from S to T
     */
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return CONVERTERS.computeIfAbsent(targetType, (key) -> new StringToEnum<T>(targetType, enumConverters));
    }

    private static class StringToEnum<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;
        private final List<EnumConverter> enumConverters;

        StringToEnum(Class<T> enumType, List<EnumConverter> enumConverters) {
            this.enumType = enumType;
            this.enumConverters = enumConverters;
        }

        /**
         * Convert the source object of type {@code S} to target type {@code T}.
         *
         * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
         * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
         * @throws IllegalArgumentException if the source cannot be converted to the desired target type
         */
        @Override
        public T convert(String source) {
            if (source.isEmpty()) {
                // It's an empty enum identifier: reset the enum value to null.
                return null;
            }
            for (EnumConverter enumConverter : enumConverters) {
                T convert = enumConverter.convert(source, enumType);
                if (convert != null) {
                    return convert;
                }
            }
            return null;
        }
    }
}
