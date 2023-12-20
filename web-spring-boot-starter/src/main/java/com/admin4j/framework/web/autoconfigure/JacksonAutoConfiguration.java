package com.admin4j.framework.web.autoconfigure;


import com.admin4j.common.time.DateFormatterPattern;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;

/**
 * @author andanyang
 * @since 2022/8/4 15:24
 */
public class JacksonAutoConfiguration {

    /**
     * 时区配置
     */
    @Bean
    @ConditionalOnClass(Jackson2ObjectMapperBuilderCustomizer.class)
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class,
                    new LocalDateTimeSerializer(DateFormatterPattern.NORM_DATETIME_FORMATTER));
            builder.deserializerByType(LocalDateTime.class,
                    new LocalDateTimeDeserializer(DateFormatterPattern.NORM_DATETIME_FORMATTER));

            builder.serializerByType(LocalDate.class,
                    new LocalDateSerializer(DateFormatterPattern.NORM_DATE_FORMATTER));
            builder.deserializerByType(LocalDate.class,
                    new LocalDateDeserializer(DateFormatterPattern.NORM_DATE_FORMATTER));


            builder.serializerByType(LocalTime.class,
                    new LocalDateSerializer(DateFormatterPattern.NORM_TIME_FORMATTER));
            builder.deserializerByType(LocalTime.class,
                    new LocalTimeDeserializer(DateFormatterPattern.NORM_TIME_FORMATTER));

            builder.failOnUnknownProperties(false);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.timeZone(TimeZone.getDefault());

            // 防止前端Long型丢失精度
            builder.serializerByType(Long.class, ToStringSerializer.instance);
        };
    }
}
