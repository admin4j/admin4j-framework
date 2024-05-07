## LogbackMDCAdapter

LogbackMDCAdapter 中使用 ThreadLocal 多线程会造成子线程TID为空，使用 TransmittableThreadLocal/InheritableThreadLocal
也没有，应为InheritableThreadLocal 存了 Map
子线程服用时 只复制了 Map 没有进行深拷贝，所以挡在 Filter 中 remove 时，子线程的Map 也会remove调，解决方案使用
TaskDecorator 拷贝一份

## 分布式链路跟踪

> 在分布式服务架构下，一个 Web 请求从网关流入，有可能会调用多个服务对请求进行处理，拿到最终结果。在这个过程中每个服务之间的通信又是单独的网络请求，无论请求流经的哪个服务除了故障或者处理过慢都会对前端造成影响。

### 一、相关概念

在分布式链路追踪中有两个重要的概念：跟踪（trace）和 跨度（span）。trace 是请求在分布式系统中的整个链路视图，span
则代表整个链路中不同服务内部的视图，span 组合在一起就是整个 trace 的视图。

#### traceId

用于标识某一次具体的请求ID。当用户的请求进入系统后，会在RPC调用网络的第一层生成一个全局唯一的traceId，并且会随着每一层的RPC调用，不断往后传递，这样的话通过traceId就可以把一次用户请求在系统中调用的路径串联起来。

#### spanId

用于标识一次RPC调用在分布式请求中的位置。请求到达每个服务后，服务都会为请求生成spanId。当用户的请求进入系统后，处在RPC调用网络的第一层A时spanId初始值是0，进入下一层RPC调用B的时候spanId是0.1，继续进入下一层RPC调用C时spanId是0.1.1，而与B处在同一层的RPC调用D的spanId是0.2，这样的话通过spanId就可以定位某一次RPC请求在系统调用中所处的位置，以及它的上下游依赖分别是谁。

#### parent-spanId

用于标识上游RPC调用在分布式请求中的位置。请求到达每个服务后，随请求一起从上游传过来的上游服务的 spanId 会被记录成
parent-spanId，或者叫 pspanId。当前服务生成的 spanId 随着请求一起，在传到下游服务时，这个 spanId 又会被下游服务当作
parent-spanId 记录。

#### MDC

MDC：（Mapped Diagnostic Context）映射诊断环境，是 log4j 和 logback 提供的一种方便在线多线程条件下记录日志的功能，可以看成是一个与当前线程绑定的
ThreadLocal。

### 二、 SpanId 生成规则

SpanId 代表本次调用在整个调用链路树中的位置。

假设一个 Web 系统 A 接收了一次用户请求，那么在这个系统的 SOFATracer MVC 日志中，记录下的 SpanId 是 0，代表是整个调用的根节点，如果
A 系统处理这次请求，需要通过 RPC 依次调用 B、C、D 三个系统，那么在 A 系统的 SOFATracer RPC 客户端日志中，SpanId 分别是
0.1，0.2 和 0.3，在 B、C、D 三个系统的 SOFATracer RPC 服务端日志中，SpanId 也分别是 0.1，0.2 和 0.3；如果 C 系统在处理请求的时候又调用了
E，F 两个系统，那么 C 系统中对应的 SOFATracer RPC 客户端日志是 0.2.1 和 0.2.2，E、F 两个系统对应的 SOFATracer RPC 服务端日志也是
0.2.1 和 0.2.2。

根据上面的描述可以知道，如果把一次调用中所有的 SpanId 收集起来，可以组成一棵完整的链路树。

假设一次分布式调用中产生的 TraceId 是 0a1234（实际不会这么短），那么根据上文 SpanId 的产生过程，如下图所示：
![](https://help-static-aliyun-doc.aliyuncs.com/assets/img/zh-CN/8703070161/p225164.png)

### RPC

RPC 支持Feign、Dubbo调用

### Logback 配置打印TID

```xml
<!--输出格式化-->
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} [%X{TID}] - %msg%n</pattern>
```

修改logback-spring.xml 文件示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <!--日志存储路径-->
    <property name="log" value="./log"/>
    <!-- 控制台输出 -->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--输出格式化-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} [%X{TID}] - %msg%n</pattern>
        </encoder>
    </appender>
    <!-- 按天生成日志文件 -->
    <appender name="file" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件名-->
            <FileNamePattern>${log}/%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>[%X{TID}] %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <!-- 日志输出级别 -->
    <root level="INFO">
        <appender-ref ref="console"/>
        <appender-ref ref="file"/>
    </root>
</configuration>
```
