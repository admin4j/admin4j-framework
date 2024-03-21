package com.admin4j.framework.lock;

import com.admin4j.framework.lock.constant.LockModel;
import lombok.Data;

/**
 * 锁信息
 *
 * @author andanyang
 * @since 2023/4/17 17:28
 */
@Data
public class LockInfo {

    /**
     * 锁资源的key
     */
    private String lockKey;
    /**
     * 锁模式
     */
    private LockModel lockModel = LockModel.REENTRANT;
    /**
     *
     */
    private boolean tryLock = false;
    private int leaseTime = -1;
    private int waitTimeOutSeconds = -1;
    private boolean tenant = false;
    private boolean user = false;

    /**
     * 锁实例
     */
    private Object lockInstance;
    /**
     * 父锁实例
     */
    private Object parentLockInstance;

    /**
     * 指定的执行器
     */
    private Class<? extends LockExecutor> executor;

}
