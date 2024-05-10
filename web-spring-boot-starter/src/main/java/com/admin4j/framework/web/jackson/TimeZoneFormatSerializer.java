package com.admin4j.framework.web.jackson;

import com.admin4j.common.time.DateFormatterPattern;
import com.admin4j.spring.util.SpringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 时间戳转 时间字符串
 *
 * @author andanyang
 * @since 2024/3/27 18:53
 */
public class TimeZoneFormatSerializer extends JsonSerializer<Long> implements ContextualSerializer {

    private TimeZoneFormat annotation;

    private TimeZoneFormatService timeZoneFormatService;

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if (value == null) {
            gen.writeNull();
        } else {

            LocalDateTime localDateTime = timeZoneFormatService.serialize(annotation.timeUnit().toMillis(value));
            gen.writeString(localDateTime.format(DateFormatterPattern.getFormatter(annotation.dateFormatter())));
        }

    }


    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        TimeZoneFormat annotation = property.getAnnotation(TimeZoneFormat.class);
        if (Objects.nonNull(annotation)) {
            this.annotation = annotation;
            this.timeZoneFormatService = SpringUtils.getBean(TimeZoneFormatService.class);
            return this;
        }

        return prov.findValueSerializer(property.getType(), property);
    }
}
