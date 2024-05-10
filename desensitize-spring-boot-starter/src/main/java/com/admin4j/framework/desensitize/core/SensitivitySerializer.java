package com.admin4j.framework.desensitize.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * @author andanyang
 * @since 2023/6/8 13:10
 */
public class SensitivitySerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitivityEnum sensitivityEnum;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            gen.writeNull();
        } else if (sensitivityEnum != SensitivityEnum.NONE) {
            gen.writeString(sensitivityEnum.desensitizer().apply(value));
        } else {
            gen.writeNull();
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {

        Sensitivity annotation = property.getAnnotation(Sensitivity.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.sensitivityEnum = annotation.strategy();
            return this;
        }

        return prov.findValueSerializer(property.getType(), property);
    }
}
