如今基本上都是分布式、多节点时代，我们业务代码中避免不了需要使用分布式锁。`admin4j-lock`
为我们提供分布式锁解决方案。支持`redisson`和`zookeeper`分布式锁

# 功能

- 支持redisson分布式锁和zookeeper 分布式锁

- 支持可重入锁

- 支持读写锁

- 支持红锁 redLock

- 支持一个注解解决分布式锁问题

- 支持一个注解解决接口幂等性问题

- 支持编程式使用分布式锁

- 锁名称支持 el表达式

# 使用方式

引入POM, 默认使用redisson分布式锁

```
<dependency>
    <groupId>com.admin4j</groupId>
    <artifactId>lock-spring-boot-starter</artifactId>
    <version>0.8.2</version>
</dependency>
```

最新版查看 [https://central.sonatype.com/artifact/com.admin4j/lock-spring-boot-starter/](https://central.sonatype.com/artifact/com.admin4j/lock-spring-boot-starter/0.2.0)

## 注解方式使用

```
 	@DistributedLock(value = "'testDLock:'+#id", user = true)
    public R testDLock(String name, Integer id) throws InterruptedException {

        Thread.sleep(30000);
        return R.ok();
    }
```

DistributedLock 注解参数详解

- prefix：锁key的前缀
- lockModel：指定锁模式 REENTRANT(可重入锁),FAIR(公平锁) ,REDLOCK(红锁),READ(读锁), WRITE(写锁)
- key、value： 锁名称,支持el 表达式
- keyGenerator： 所名称生成器。Spring注入基础DLockKeyGenerator实现类即可
- tryLock：是否尝试获取锁。成功获取则进入锁；获取失败则抛出异常。 true 获取不到锁，会立即返回，不会阻塞。false(默认)
  获取不到锁，会阻塞当前线程
- tenant： 是否开启租户（默认false）。开启租户会在可能后面拼接上租户。 需要实现 ILoginUserInfoService 接口告诉当前登录用户的租户信息
- user：是否开启用户（默认false）开启用户模式 需要实现 ILoginUserInfoService 接口告诉当前登录用户的唯一ID标识
- executor: 分布式锁执行器。指定使用 redisson 还是 zookeeper 分布式锁

# 编程式使用

```
//使用工具类
 DistributedLockUtil.tryLockWithError("DistributedLock：" + id, () -> {
            System.out.println("i get the lock   = " + name);
           //doSomething
        });
```

## 使用zookeeper  分布式锁

```
        <dependency>
            <groupId>com.admin4j</groupId>
            <artifactId>lock-spring-boot-starter</artifactId>
            <version>0.8.2</version>
            <exclusions>
                <exclusion>
                    <artifactId>lock-redisson-spring-boot-starter</artifactId>
                    <groupId>com.admin4j</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.admin4j</groupId>
            <artifactId>lock-zookeeper-spring-boot-starter</artifactId>
            <version>0.8.2</version>
        </dependency>
```

引入`lock-zookeeper-spring-boot-starter` 依赖，默认使用zookeeper分布式锁

# 指定分布式锁执行器

同时引入 zookeeper 和 redisson 。默认使用redisson，可以指定 executor 执行器来切换分布式类型

````
        <dependency>
            <groupId>com.admin4j</groupId>
            <artifactId>lock-spring-boot-starter</artifactId>
            <version>0.8.2</version>
            <exclusions>
                <exclusion>
                    <artifactId>lock-redisson-spring-boot-starter</artifactId>
                    <groupId>com.admin4j</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.admin4j</groupId>
            <artifactId>lock-zookeeper-spring-boot-starter</artifactId>
            <version>0.8.2</version>
        </dependency>
````

指定 zookeeper分布式锁 例子：

```
    @DistributedLock( value = "'testDLock:'+#id", user = true, executor = ZookeeperLockExecutor.class)
    public R testDLock(String name, Integer id) throws InterruptedException {

        Thread.sleep(30000);
        return R.ok();
    }
```

指定 redisson分布式锁 例子：

```
    @DistributedLock( value = "'testDLock:'+#id", user = true, executor = RedissonLockExecutor.class)
    public R testDLock(String name, Integer id) throws InterruptedException {

        Thread.sleep(30000);
        return R.ok();
    }
```

使用示例代码 [https://github.com/admin4j/admin4j-example](https://github.com/admin4j/admin4j-example)

# 一个注解搞定接口幂等性

```
	@GetMapping("Idempotent")
    @Idempotent(tryLock = true, key = "'Idempotent'+#id")
    public R Idempotent(String name, Integer id) throws InterruptedException {

        Thread.sleep(30000);
        return R.ok();
    }
```

需要实现 ILoginUserInfoService 接口,返回当前登录用户唯一ID

# 分布式锁原理

## 1. redis 原生命令的不足

使用redis做分布式锁相对于更简单和高效。但不是说用了redis分布式锁，就可以高枕无忧了，如果没有用好或者用对，也会引来一些意想不到的坑。

#### 1.1 非原子性操作

```
if (jedis.setnx(lockKey, val) == 1) {
   jedis.expire(lockKey, timeout);
}
```

改进方式使用

```
String result = jedis.set(lockKey, requestId, "NX", "PX", expireTime);
if ("OK".equals(result)) {
    return true;
}
return false;
```

#### 1.2 释放了别人的锁

假如线程A和线程B，都使用lockKey加锁。线程A加锁成功了，但是由于业务功能耗时时间很长，超过了设置的超时时间。这时候，redis会自动释放lockKey锁。此时，线程B就能给lockKey加锁成功了，接下来执行它的业务操作。恰好这个时候，线程A执行完了业务功能，接下来，在finally方法中释放了锁lockKey。这不就出问题了，线程B的锁，被线程A释放了。不知道你们注意到没？在使用`set`
命令加锁时，除了使用lockKey锁标识，还多设置了一个参数：`requestId`，为什么要需要记录requestId呢？

答：requestId是在释放锁的时候用的。

在释放锁的时候，先获取到该锁的值（之前设置值就是requestId），然后判断跟之前设置的值是否相同，如果相同才允许删除锁，返回成功。如果不同，则直接返回失败。

此外，使用lua脚本，也能解决释放了别人的锁的问题：

```
if redis.call('get', KEYS[1]) == ARGV[1] then 
 return redis.call('del', KEYS[1]) 
else 
  return 0 
end
```

#### 1.3 抢不到锁的线程不会阻塞，大量失败请求

当大量请求进入时，只有一个会成功，其他的都是失败。每1万个请求，有1个成功。再1万个请求，有1个成功。如此下去，直到库存不足。这就变成均匀分布的秒杀了，跟我们想象中的不一样。

#### 1.4 不支持锁重入问题

#### 1.5 锁竞争问题，不支持读写锁，锁颗粒度大

如果有大量需要写入数据的业务场景，使用普通的redis分布式锁是没有问题的。

但如果有些业务场景，写入的操作比较少，反而有大量读取的操作。这样直接使用普通的redis分布式锁，会不会有点浪费性能？

我们都知道，锁的粒度越粗，多个线程抢锁时竞争就越激烈，造成多个线程锁等待的时间也就越长，性能也就越差。

所以，提升redis分布式锁性能的第一步，就是要把锁的粒度变细。添加读写锁

#### 1.6 锁超时问题

如果线程A加锁成功了，但是由于业务功能耗时时间很长，超过了设置的超时时间，这时候redis会自动释放线程A加的锁。其他线程就会抢到锁，但是A线程还未结束

#### 1.7 主从复制的问题

如果redis存在多个实例。比如：做了主从，或者使用了哨兵模式，由于redis 主从复制是异步的（AP模型） 就会出现问题。可以通过redLock
解决

## 2. redisson 分布式锁接口方案

使用原生的redis 会有各种问题，我们来看看redisson框架给我的解决方法

#### 2.1 看门狗原理

如果负责储存这个分布式锁的 Redisson
节点宕机以后，而且这个锁正好处于锁住的状态时，这个锁会出现锁死的状态。为了避免这种情况的发生，Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。

默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定。

如果我们未制定 lock 的超时时间，就使用 30 秒作为看门狗的默认时间。只要占锁成功，就会启动一个定时任务：每隔 10
秒重新给锁设置过期的时间，过期时间为 30 秒。

#### 2.2 主从复制的问题

提供了一个专门的类：`RedissonRedLock`，使用了Redlock算法。

Redisson
原理参考 [https://blog.csdn.net/agonie201218/article/details/115339670](https://blog.csdn.net/agonie201218/article/details/115339670)

redisson
操作示例 [https://blog.csdn.net/agonie201218/article/details/122084140](https://blog.csdn.net/agonie201218/article/details/122084140)

redis分布式锁的坑 [https://blog.csdn.net/agonie201218/article/details/121423212](https://blog.csdn.net/agonie201218/article/details/121423212)

## 3.Zookeeper 分布式锁接口方案

zk 分布式锁，其实可以做的比较简单，就是某个节点尝试创建临时 znode，此时创建成功了就获取了这个锁；这个时候别的客户端来创建锁会失败，只能
**注册个监听器**监听这个锁。释放锁就是删除这个 znode，一旦释放掉就会通知客户端，然后有一个等待着的客户端就可以再次重新加锁。

#### 参考

java分布式锁解决方案 redisson or
ZooKeeper [https://blog.csdn.net/agonie201218/article/details/122446601](https://blog.csdn.net/agonie201218/article/details/122446601)

万字总结Zookeeper客户端Curator操作Api [https://andyoung.blog.csdn.net/article/details/130115913](https://andyoung.blog.csdn.net/article/details/130115913)

# 项目地址

[https://github.com/admin4j/admin4j-framework/tree/master/admin4j-lock](https://github.com/admin4j/admin4j-framework/tree/master/admin4j-lock)