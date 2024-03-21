package com.admin4j.framework.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andanyang
 * @since 2024/1/17 15:26
 */
@SpringBootTest(classes = LockMain.class, properties = "logging.level.root=error")
public class LockSpringTest {


    static RedissonLockExecutor lockExecutor;
    @Autowired
    RedissonClient redissonClient;

    @BeforeEach
    public void setUp() throws Exception {
        lockExecutor = new RedissonLockExecutor(redissonClient);
        lockExecutor.setParent(new LocalLockExecutor());
    }

    public LockInfo getLock(String lockKey) {


        LockInfo lockInfo = new LockInfo();
        lockInfo.setLockKey(lockKey);
        lockExecutor.initSetLockInstance(lockInfo);

        return lockInfo;
    }

    public void lock(String lockKey, int i) {
        LockInfo testLock = getLock(lockKey);
        // testLock.setWaitTimeOutSeconds(10000);
        lockExecutor.lock(testLock);
        System.out.println("lock  getLock: " + testLock.getLockKey() + " " + i);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("unlock  LockKey: " + testLock.getLockKey() + " " + i);
        unlock(testLock);
    }

    public void tryLock(String lockKey, int i) {
        LockInfo testLock = getLock(lockKey);
        // testLock.setWaitTimeOutSeconds(10000);
        if (lockExecutor.tryLock(testLock)) {
            System.out.println("lock  tryLock: " + testLock.getLockKey() + " " + i);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("unlock  tryLock: " + testLock.getLockKey() + " " + i);
            unlock(testLock);
        } else {
            System.out.println("tryLock failed: " + testLock.getLockKey() + " " + i);
        }
    }

    public void unlock(LockInfo testLock) {
        lockExecutor.unlock(testLock);
    }

    @Test
    public void testLock() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {

            int finalI = i;
            Thread thread = new Thread(() -> {

                try {
                    lock("key:" + finalI / 3, finalI);
                } catch (Exception e) {
                    System.out.println("Exception = " + Thread.currentThread().getName() + " " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }


            });
            thread.setName("A-LOCK_" + i);
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
                    Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
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

}
