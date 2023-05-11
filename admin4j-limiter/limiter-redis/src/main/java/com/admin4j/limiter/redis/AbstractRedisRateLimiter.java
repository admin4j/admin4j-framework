package com.admin4j.limiter.redis;

import com.admin4j.limiter.core.RateLimiterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * @author andanyang
 * @since 2023/5/11 10:43
 */
public abstract class AbstractRedisRateLimiter implements RateLimiterProvider {

    private static final String RATE_LIMIT_KEY_PREFIX = "RL:";

    private RedisScript<Long> rateLimitScript;

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //public AbstractRedisRateLimiter(StringRedisTemplate stringRedisTemplate) {
    //    this.stringRedisTemplate = stringRedisTemplate;
    //}
    
    @PostConstruct
    public void init() {

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        ClassPathResource resource = new ClassPathResource(getScriptName());
        script.setScriptSource(new ResourceScriptSource(resource));
        script.setResultType(Long.class);
        this.rateLimitScript = script;
    }

    protected abstract String getScriptName();

    protected String getKeyPrefix() {
        return RATE_LIMIT_KEY_PREFIX;
    }

    /**
     * 判断请求是否允许通过
     *
     * @param key
     * @param qps
     * @param time
     * @return
     */
    @Override
    public boolean tryAcquire(String key, int qps, int time) {

        Object result = stringRedisTemplate.execute(rateLimitScript, Collections.singletonList(getKeyPrefix() + key),
                Integer.toString(qps),
                Integer.toString(time));
        return (Long) result == 1L;
    }
}
