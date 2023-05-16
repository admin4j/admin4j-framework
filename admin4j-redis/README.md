# Redis 序列化器 starter

redis序列化器自动装配starter。默认使用默认使用jackson，支持fastjson,fastjson2

# 使用方式

- pom 使用默认的 jackson

```xml

<dependency>
    <groupId>com.admin4j.redis</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
</dependency>
```

- pom 使用默认的 fastjson

```
<dependency>
    <groupId>com.admin4j.redis</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>com.admin4j.redis</groupId>
    <artifactId>admin4j-redis-fastjson</artifactId>
</dependency>
<!--    (导入自己的fastjson 版本)-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
</dependency>
```

- pom 使用默认的 fastjson

```
<dependency>
    <groupId>com.admin4j.redis</groupId>
    <artifactId>redis-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>com.admin4j.redis</groupId>
    <artifactId>admin4j-redis-fastjson</artifactId>
</dependency>

<!--    (导入自己的fastjson2 版本)-->
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2</artifactId>
</dependency>
```

# 用例

[https://github.com/admin4j/admin4j-example](https://github.com/admin4j/admin4j-example)