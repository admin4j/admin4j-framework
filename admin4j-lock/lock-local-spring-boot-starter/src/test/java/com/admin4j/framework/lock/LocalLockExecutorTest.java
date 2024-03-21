package com.admin4j.framework.lock;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author andanyang
 * @since 2024/1/16 16:11
 */
public class LocalLockExecutorTest {

    LocalLockExecutor executor = new LocalLockExecutor();

    public LockInfo getLock(String lockKey) {


        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockKey(lockKey);
        executor.initSetLockInstance(lockInfo);

        return lockInfo;
    }

    public void lock(String lockKey, int i) {
        LockInfo testLock = getLock(lockKey);
        // testLock.setWaitTimeOutSeconds(10000);
        executor.lock(testLock);
        System.out.println("lock  getLock: " + testLock.getLockKey() + " " + i);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("unlock  LockKey: " + testLock.getLockKey() + " " + i);
        unlock(testLock);
    }

    public void tryLock(String lockKey, int i) {
        LockInfo testLock = getLock(lockKey);
        // testLock.setWaitTimeOutSeconds(10000);
        if (executor.tryLock(testLock)) {
            System.out.println("lock  tryLock: " + testLock.getLockKey() + " " + i);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("unlock  tryLock: " + testLock.getLockKey() + " " + i);
            unlock(testLock);
        }
        System.out.println("tryLock failed: " + testLock.getLockKey() + " " + i);

    }

    public void unlock(LockInfo testLock) {
        executor.unlock(testLock);
    }

    @Test
    public void testLock() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {

            int finalI = i;
            Thread thread = new Thread(() -> {

                lock("key:" + finalI / 3, finalI);
                countDownLatch.countDown();
            });

            thread.start();

        }

        countDownLatch.await();
    }

    @Test
    public void testTryLock() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {

            int finalI = i;
            Thread thread = new Thread(() -> {

                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                tryLock("key:" + finalI / 10, finalI);
                countDownLatch.countDown();
            });

            thread.start();

        }

        countDownLatch.await();
    }


    @Test
    public void testUNUnLock() {

        ReentrantLock lock = new ReentrantLock();

        lock.lock();

        System.out.println("lock = " + lock);

        lock.unlock();
        lock.isLocked();


    }
}