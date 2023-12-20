package com.admin4j.elasticsearch.autoconfigure;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.admin4j.common.time.DateFormatterPattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;

/**
 * @author andanyang
 * @since 2023/12/7 16:18
 */
@ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
public class JsonpMapperAutoconfigure {

    @Bean
    @ConditionalOnMissingBean(JsonpMapper.class)
    public JsonpMapper esJsonpMapper() {
        
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, false)
                .configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL).setTimeZone(TimeZone.getDefault());

        JavaTimeModule module = new JavaTimeModule();

        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateFormatterPattern.NORM_DATETIME_FORMATTER));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateFormatterPattern.NORM_DATETIME_FORMATTER));

        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateFormatterPattern.NORM_DATE_FORMATTER));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateFormatterPattern.NORM_DATE_FORMATTER));

        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateFormatterPattern.NORM_TIME_FORMATTER));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateFormatterPattern.NORM_TIME_FORMATTER));

        objectMapper.registerModule(module);

        return new JacksonJsonpMapper(objectMapper);
    }
}
