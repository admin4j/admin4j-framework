package com.admin4j.framework.lock.key;

import java.lang.reflect.Method;

/**
 * 为 @DistributedLock 生成key
 *
 * @author andanyang
 * @since 2020/3/13 15:18
 */
@FunctionalInterface
public interface DLockKeyGenerator {

    /**
     * Generate a key for the given method and its parameters.
     *
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    Object generate(Object target, Method method, Object... params);
}
