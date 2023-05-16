package com.admin4j.redis.factory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author andanyang
 * @since 2023/5/16 11:11
 */
public class RedisSerializerFactory {

    public static <K, V> RedisTemplate defaultRedisSerializer(RedisConnectionFactory connectionFactory) {
        //jackson序列化工具
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        return serializer(connectionFactory, jackson2JsonRedisSerializer);

    }

    public static <K, V> RedisTemplate<K, V> serializer(RedisConnectionFactory connectionFactory, RedisSerializer<V> serializer) {

        RedisTemplate<K, V> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(serializer);
        template.setDefaultSerializer(RedisSerializer.string());
        template.afterPropertiesSet();
        return template;
    }
}
