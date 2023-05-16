package com.admin4j.redis;

import org.springframework.data.redis.serializer.RedisSerializer;

import static java.lang.Integer.compare;

/**
 * @author andanyang
 * @since 2023/5/16 10:08
 */
public interface RedisValueSerializer<T> extends RedisSerializer<T>, Comparable<RedisValueSerializer> {

    default int order() {
        return 0;
    }

    @Override
    default int compareTo(RedisValueSerializer o) {
        if (o != null) {
            return compare(o.order(), order());
        }
        return -1;
    }
}
