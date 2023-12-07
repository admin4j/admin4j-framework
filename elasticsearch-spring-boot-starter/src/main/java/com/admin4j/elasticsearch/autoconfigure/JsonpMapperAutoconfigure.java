package com.admin4j.elasticsearch.autoconfigure;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.registerModule(new JavaTimeModule());

        return new JacksonJsonpMapper(objectMapper);
    }
}
