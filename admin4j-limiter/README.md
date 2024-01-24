# RateLimiter

基于redis的接口限流器。支持固定窗口，滑动窗口，滑动日志，漏桶算法，令牌桶算法

# TODO

添加本地锁
https://mp.weixin.qq.com/s/1mBorW_B2xvJ_8FjE_JrCw

# 使用方式

- pom 使用默认的 jackson

```xml

<dependency>
    <groupId>com.admin4j.limiter</groupId>
    <artifactId>limiter-redis</artifactId>
</dependency>
<dependency>
<groupId>com.admin4j.redis</groupId>
<artifactId>redis-spring-boot-starter</artifactId>
</dependency>
```

# 用例

```java

@RestController
@RequestMapping("limiter")
public class LimiterController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("SLIDING_WINDOW")
    @RateLimiter(limiterType = LimiterType.SLIDING_WINDOW, maxAttempts = 5, interval = 10)
    public R SLIDING_WINDOW(String name, Integer id) {

        User user = new User(name, id, "男");
        return R.ok(user);
    }

    @GetMapping("FIX_WINDOW")
    @RateLimiter(limiterType = LimiterType.FIX_WINDOW, maxAttempts = 5, interval = 10)
    public R FIX_WINDOW(String name, Integer id) {

        User user = new User(name, id, "男");
        return R.ok(user);
    }

    @GetMapping("SLIDING_LOG")
    @RateLimiter(limiterType = LimiterType.SLIDING_LOG, maxAttempts = 5, interval = 10)
    public R SLIDING_LOG(String name, Integer id) {

        User user = new User(name, id, "男");
        return R.ok(user);
    }

    @GetMapping("LEAKY_BUCKET")
    @RateLimiter(limiterType = LimiterType.LEAKY_BUCKET, maxAttempts = 5, interval = 10)
    public R LEAKY_BUCKET(String name, Integer id) {

        User user = new User(name, id, "男");
        return R.ok(user);
    }

    @GetMapping("TOKEN_BUCKET")
    @RateLimiter(limiterType = LimiterType.TOKEN_BUCKET, maxAttempts = 5, interval = 10)
    public R TOKEN_BUCKET(String name, Integer id) {

        User user = new User(name, id, "男");
        return R.ok(user);
    }

    @GetMapping("test")
    public R test(String name, Integer id) {

        User user = new User(name, id, id == 1 ? "男" : "女");
        return R.ok(user);
    }
}
```

[https://github.com/admin4j/admin4j-example](https://github.com/admin4j/admin4j-example/blob/master/src/main/java/com/admin4j/limiter/controller/LimiterController.java)