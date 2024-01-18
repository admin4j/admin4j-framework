package com.admin4j.framework.lock.pool;

import com.admin4j.framework.lock.constant.LockModel;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author andanyang
 * @since 2024/1/17 17:31
 */
public class PooledLockFactory implements KeyedPooledObjectFactory<LockModel, WrapperLockObject> {

    /**
     * 对象被激活后，会进行调用
     * Reinitializes an instance to be returned by the pool.
     *
     * @param key the key used when selecting the object
     * @param p   a {@code PooledObject} wrapping the instance to be activated
     * @throws Exception if there is a problem activating {@code obj},
     *                   this exception may be swallowed by the pool.
     * @see #destroyObject
     */
    @Override
    public void activateObject(LockModel key, PooledObject<WrapperLockObject> p) throws Exception {

    }

    /**
     * 销毁
     * Destroys an instance no longer needed by the pool.
     * <p>
     * It is important for implementations of this method to be aware that there
     * is no guarantee about what state {@code obj} will be in and the
     * implementation should be prepared to handle unexpected errors.
     * </p>
     * <p>
     * Also, an implementation must take in to consideration that instances lost
     * to the garbage collector may never be destroyed.
     * </p>
     *
     * @param key the key used when selecting the instance
     * @param p   a {@code PooledObject} wrapping the instance to be destroyed
     * @throws Exception should be avoided as it may be swallowed by
     *                   the pool implementation.
     * @see #validateObject
     * @see KeyedObjectPool#invalidateObject
     */
    @Override
    public void destroyObject(LockModel key, PooledObject<WrapperLockObject> p) throws Exception {

    }

    /**
     * 创建一个对象
     * Creates an instance that can be served by the pool and
     * wrap it in a {@link PooledObject} to be managed by the pool.
     *
     * @param key the key used when constructing the object
     * @return a {@code PooledObject} wrapping an instance that can
     * be served by the pool.
     * @throws Exception if there is a problem creating a new instance,
     *                   this will be propagated to the code requesting an object.
     */
    @Override
    public PooledObject<WrapperLockObject> makeObject(LockModel keyModel) throws Exception {

        WrapperLockObject wrapperLock;
        switch (keyModel) {
            case FAIR:
                // 公平锁
                wrapperLock = new WrapperLockObject(new ReentrantLock(true));
                break;
            case READ:
            case WRITE:
                // 读之前加读锁，读锁的作用就是等待该lockkey释放写锁以后再读
                ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
                wrapperLock = new WrapperReadWriteLockObject(reentrantReadWriteLock);
                break;

            // // 写之前加写锁，写锁加锁成功，读锁只能等待
            // ReentrantReadWriteLock reentrantReadWriteLock1 = new ReentrantReadWriteLock();
            // object = reentrantReadWriteLock1.writeLock();
            // break;
            case REENTRANT:
            default:
                // 可重入锁
                wrapperLock = new WrapperLockObject(new ReentrantLock());
        }

        return new DefaultPooledObject<>(wrapperLock);
    }

    /**
     * 回收资源时候进行调用
     * Uninitializes an instance to be returned to the idle object pool.
     *
     * @param key the key used when selecting the object
     * @param p   a {@code PooledObject} wrapping the instance to be passivated
     * @throws Exception if there is a problem passivating {@code obj},
     *                   this exception may be swallowed by the pool.
     * @see #destroyObject
     */
    @Override
    public void passivateObject(LockModel key, PooledObject<WrapperLockObject> p) throws Exception {
        p.getObject().clear();
    }

    /**
     * 如果连接关闭说明已经失效就返回false告诉池子,已经失效,会自动移除
     * Ensures that the instance is safe to be returned by the pool.
     *
     * @param key the key used when selecting the object
     * @param p   a {@code PooledObject} wrapping the instance to be validated
     * @return {@code false} if {@code obj} is not valid and should
     * be dropped from the pool, {@code true} otherwise.
     */
    @Override
    public boolean validateObject(LockModel key, PooledObject<WrapperLockObject> p) {
        return false;
    }

    /**
     * Destroys an instance no longer needed by the pool, using the provided {@link DestroyMode}.
     *
     * @param key         the key used when selecting the instance
     * @param p           a {@code PooledObject} wrapping the instance to be destroyed
     * @param destroyMode DestroyMode providing context to the factory
     * @throws Exception should be avoided as it may be swallowed by
     *                   the pool implementation.
     * @see #validateObject
     * @see KeyedObjectPool#invalidateObject
     * @see #destroyObject(Object, PooledObject)
     * @see DestroyMode
     * @since 2.9.0
     */
    @Override
    public void destroyObject(LockModel key, PooledObject<WrapperLockObject> p, DestroyMode destroyMode) throws Exception {
        KeyedPooledObjectFactory.super.destroyObject(key, p, destroyMode);
    }
}
