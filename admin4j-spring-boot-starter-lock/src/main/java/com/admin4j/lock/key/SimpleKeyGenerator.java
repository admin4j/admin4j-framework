package com.admin4j.lock.key;

import java.lang.reflect.Method;

/**
 * 简单的Key 生成器
 *
 * @author andanyang
 * @since 2020/3/13 15:21
 */
public class SimpleKeyGenerator implements DLockKeyGenerator {

    /**
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return 返回 ClassName:methodName 格式
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {

        return target.getClass().getCanonicalName() + ":" + method.getName();
    }


}
