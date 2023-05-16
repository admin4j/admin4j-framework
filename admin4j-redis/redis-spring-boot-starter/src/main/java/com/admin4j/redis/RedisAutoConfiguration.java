package com.admin4j.redis;

import com.admin4j.redis.factory.RedisSerializerFactory;
import com.admin4j.redis.fastjson.FastJson22JsonRedisSerializer;
import com.admin4j.redis.fastjson.FastJson2JsonRedisSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author andanyang
 * @since 2023/5/15 13:35
 */
@Configuration
@AutoConfigureBefore(org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
public class RedisAutoConfiguration {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean("redisTemplate")
    @ConditionalOnClass(name = {"com.admin4j.redis.fastjson.FastJson2JsonRedisSerializer"})
    public <K, V> RedisTemplate<K, V> fastJSONRedisTemplate(RedisConnectionFactory connectionFactory) {
        return (RedisTemplate<K, V>) RedisSerializerFactory.serializer(connectionFactory, new FastJson2JsonRedisSerializer<>(Object.class));
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean("redisTemplate")
    @ConditionalOnClass(name = {"com.admin4j.redis.fastjson.FastJson22JsonRedisSerializer"})
    public <K, V> RedisTemplate<K, V> fastJSON2RedisTemplate(RedisConnectionFactory connectionFactory) {
        return (RedisTemplate<K, V>) RedisSerializerFactory.serializer(connectionFactory, new FastJson22JsonRedisSerializer<>(Object.class));
    }

    @Bean("redisTemplate")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean(name = "redisTemplate")
    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory connectionFactory) {
        return RedisSerializerFactory.defaultRedisSerializer(connectionFactory);
    }

    @Bean
    public RedisService redisService(RedisTemplate redisTemplate) {
        return new RedisService(redisTemplate);
    }

    /**
     * 默认的 序列化
     *
     * @return
     */

    //
    //@Bean("redisTemplate")
    //@ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    //@Order(2)
    //public <K, V> RedisTemplate<K, V> jacksonRedisTemplate(RedisConnectionFactory connectionFactory) {
    //
    //    //jackson序列化工具
    //    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    //    ObjectMapper om = new ObjectMapper();
    //    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    //    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    //    jackson2JsonRedisSerializer.setObjectMapper(om);
    //
    //    //1.创建
    //    RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
    //    redisTemplate.setConnectionFactory(connectionFactory);
    //
    //    //设置key的序列化
    //    redisTemplate.setKeySerializer(RedisSerializer.string());
    //    redisTemplate.setHashKeySerializer(RedisSerializer.string());
    //    //设置值的value
    //    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    //    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    //    redisTemplate.setDefaultSerializer(RedisSerializer.string());
    //    redisTemplate.afterPropertiesSet();
    //    return redisTemplate;
    //}
    //
    //
    //@Order(3)
    //@Bean("redisTemplate")
    //@ConditionalOnClass(name = "com.alibaba.fastjson2.JSON")
    //public <K, V> RedisTemplate<K, V> fastJSON2RedisTemplate(RedisConnectionFactory connectionFactory) {
    //
    //    RedisSerializer serializer = new FastJson22JsonRedisSerializer(Object.class);
    //
    //    RedisTemplate<K, V> template = new RedisTemplate<>();
    //    template.setConnectionFactory(connectionFactory);
    //
    //    template.setKeySerializer(RedisSerializer.string());
    //    template.setValueSerializer(serializer);
    //    template.setHashKeySerializer(RedisSerializer.string());
    //    template.setHashValueSerializer(serializer);
    //    template.setDefaultSerializer(RedisSerializer.string());
    //    template.afterPropertiesSet();
    //    return template;
    //}

}
