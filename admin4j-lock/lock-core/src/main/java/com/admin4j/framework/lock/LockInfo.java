package com.admin4j.framework.lock;

import com.admin4j.framework.lock.constant.LockModel;
import com.admin4j.framework.lock.util.DistributedLockUtil;
import lombok.Data;

/**
 * 锁信息
 *
 * @author andanyang
 * @since 2023/4/17 17:28
 */
@Data
public class LockInfo<T> {

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
    private T lockInstance;

    /**
     * 指定的执行器
     */
    private Class<? extends LockExecutor> executor;

    public void setLockKey(String lockKey) {
        this.lockKey = DistributedLockUtil.DISTRIBUTED_LOCK_PRE + lockKey;
    }
}
