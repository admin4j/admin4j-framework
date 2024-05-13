# 引入依赖

```xml

<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>prometheus-spring-boot-starter</artifactId>
    <version>0.10.0</version>
</dependency>
```

[最新版查看](https://central.sonatype.com/artifact/com.admin4j.framework/prometheus-spring-boot-starter)

# CONFIG

修改配置文件application.yml

```yml
spring:
  application:
    name: "you app name"

management:
  endpoints:
    web:
      exposure:
        include:
          - prometheus
          - health
```

# 参考监控数据 http://localhost:8080/actuator/prometheus

# 参考

https://blog.csdn.net/agonie201218/article/details/126337841
https://blog.csdn.net/agonie201218/article/details/128369743