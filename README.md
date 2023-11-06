# admin4j framework

这是一个企业级开箱即用的SpringBoot starter项目，经过数年的公司内部开发和大量项目使用，总结并封装了常规业务所需的框架功能。它使用了注解或者其他工具类方法的方式，极大地简化了开发配置，并可自由组合各个模块。

这个项目是我们在SpringBoot方面的知识和经验的结晶，旨在为您的项目提供便利和效率。


## 主要功能

| 项目                                                         | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [admin4j-lock](admin4j/admin4j-framework/tree/master/admin4j-lock) | 分布式锁。支持Redisson, zookeeper                            |
| [admin4j-limiter](/admin4j/admin4j-framework/tree/master/admin4j-limiter) | 限速器。支持固定窗口，滑动窗口，滑动日志，漏桶算法，令牌桶算法 |
| [admin4j-redis](/admin4j/admin4j-framework/tree/master/admin4j-redis) | Redis 序列化器。默认使用默认使用jackson，支持fastjson,fastjson2。 |
| [desensitize-spring-boot-starter](/admin4j/admin4j-framework/tree/master/desensitize-spring-boot-starter) | 接口数据脱敏。支持用户名，手机号，身份证号，邮件，地址等     |
| [elasticsearch-spring-boot-starter](/admin4j/admin4j-framework/tree/master/elasticsearch-spring-boot-starter) | es 连接器                                                    |
| [enum-spring-boot-starter](/admin4j/admin4j-framework/tree/master/enum-spring-boot-starter) | 优雅的使用枚举参数                                           |
| [excel-spring-boot-starter](/admin4j/admin4j-framework/tree/master/excel-spring-boot-starter) | easyexcel 使用工具类                                         |
| [feign-spring-boot-starter](/admin4j/admin4j-framework/tree/master/feign-spring-boot-starter) | feign 封装。 使用OKHttp 发送feig                             |
| [kaptcha-spring-boot-starter](/admin4j/admin4j-framework/tree/master/kaptcha-spring-boot-starter) | 验证码                                                       |
| [log-spring-boot-starter](/admin4j/admin4j-framework/tree/master/log-spring-boot-starter) | 系统日志                                                     |
| [mybatis-plus-boot-starter](/admin4j/admin4j-framework/tree/master/mybatis-plus-boot-starter) | mybatis-plus 封装                                            |
| [oss-spring-boot-starter](/admin4j/admin4j-framework/tree/master/oss-spring-boot-starter) | OSS 封装。解决大部分OSS平台兼容问题                          |
| [prometheus-spring-boot-starter](/admin4j/admin4j-framework/tree/master/prometheus-spring-boot-starter) | prometheus接口封装                                           |
| [security-spring-boot-starter](/admin4j/admin4j-framework/tree/master/security-spring-boot-starter) | spring security 封装。支持多渠道登录                         |
| [tenant-spring-boot-starter](/admin4j/admin4j-framework/tree/master/tenant-spring-boot-starter) | 多租户封装                                                   |
| [test-spring-boot-starter](/admin4j/admin4j-framework/tree/master/test-spring-boot-starter) | Spring boot 测试类封装。默认添加用户登录 UserContext 环境    |
| [ttl-spring-boot-starter](https://gitee.com/admin4j/admin4j-framework/tree/master/ttl-spring-boot-starter) | 多线程框架封装。使用阿里的 transmittable-thread-local 框架 实现多线程之间的thread-local传值，封装多线程操作 |
| [xss-spring-boot-starter](/admin4j/admin4j-framework/tree/master/xss-spring-boot-starter) | xxs 防止脚本注入                                             |
| [xxl-job-spring-boot-starter](/admin4j/admin4j-framework/tree/master/xxl-job-spring-boot-starter) | xxl 任务core封装                                             |
| [zookeeper-spring-boot-starter](/admin4j/admin4j-framework/tree/master/zookeeper-spring-boot-starter) | zookeeper 连接器                                             |
| [admin4j-dependencies](/admin4j/admin4j-framework/tree/master/admin4j-dependencies) | admin4j 项目各个版本依赖                                     |
| [admin4j-common](/admin4j/admin4j-framework/tree/master/admin4j-common) | 基础工具类                                                   |
| [admin4j-common-spring](/admin4j/admin4j-framework/tree/master/admin4j-common-spring) | 基础Spring工具类                                             |
| [admin4j-common-spring-web](/admin4j/admin4j-framework/tree/master/admin4j-common-spring-web) | spring web 工具类                                            |
| [admin4j-parent](/admin4j/admin4j-framework/tree/master/admin4j-parent) | admin4j 项目父项目工程，引入项目依赖                         |
| [spring-boot-parent](/admin4j/admin4j-framework/tree/master/spring-boot-parent) | admin4j spring-boot 项目父项目工程，引入项目依赖             |
| [common-http](https://github.com/admin4j/common-http)        | 一个HTTP 请求库。HTTP request library packaged specifically for JAVA |
| [admin4j-dict](https://github.com/admin4j/admin4j-dict)      | 字典装换                                                     |
| [spring-plugin](https://github.com/admin4j/spring-plugin)    | 基于Spring实现，极轻巧的设计模式插件                         |
| [admin4j-json](https://github.com/admin4j/admin4j-json)      | JSON 适配器工具类。适配各大框架 fastjson，fastjson2，jackson，gson等 |

## 使用

所有项目都已经发布到Maven Central中央仓库，您只需要直接引用对应的pom坐标依赖即可。单击每个项目以查看使用方法和示例。
