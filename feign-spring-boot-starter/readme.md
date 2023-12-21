基础使用 [OpenFeign 全方位讲解](https://blog.csdn.net/agonie201218/article/details/121154769)

## 1. 生产环境 OpenFeign 的配置事项

### 1.1 如何更改 OpenFeign 默认的负载均衡策略

```
warehouse-service: #服务提供者的微服务ID
  ribbon:
    #设置对应的负载均衡类
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

### 1.2 开启默认的 OpenFeign 数据压缩功能

```
feign:
  compression:
    request:
      # 开启请求数据的压缩功能
      enabled: true
      # 压缩支持的MIME类型
      mime-types: text/xml,application/xml, application/json
      # 数据压缩下限 1024表示传输数据大于1024 才会进行数据压缩(最小压缩值标准)
      min-request-size: 1024
    # 开启响应数据的压缩功能
    response:
      enabled: true
```

### 1.3 替换默认通信组件

- 1.引入 feign-okhttp 依赖包。

  ```
  <dependency>
      <groupId>io.github.openfeign</groupId>
      <artifactId>feign-okhttp</artifactId>
  </dependency>
  ```

- 3.在 application.yml 中启用 OkHttp

  ```
  feign:
    okhttp:
      enabled: true
    httpclient:
      enabled: false
    client:
      config:
        default:
          loggerLevel: HEADERS
          connectTimeout: 10000
          readTimeout: 10000
    compression:
      request:
        enabled: true
      response:
        enabled: true
  ```

- #### ribbon中的Http Client

  通过OpenFeign作为注册中心的客户端时，默认使用Ribbon做负载均衡，Ribbon默认也是用jdk自带的HttpURLConnection，需要给Ribbon也设置一个Http client，比如使用okhttp，在properties文件中增加下面配置：

  ```
  ribbon.okhttp.enabled=true
  ```

  



## 2. 启用日志

### 2.1 配置日志级别

```
logging:
  level:
    "com.gkcrm.workflow.api": DEBUG
```

### 2.2 配置 feign 日记级别

````
feign:
  okhttp:
    enabled: true
  httpclient:
    enabled: false
  client:
    config:
      default:
        loggerLevel: HEADERS
````

或者

```
@Configuration
public class FeignLogConfig {
    @Bean
    public Logger.Level feignLogLevel(){
         /**
         *   日志级别：
         *   NONE（不记录日志 (默认)）
         *   BASIC（只记录请求方法和URL以及响应状态代码和执行时间）
         *   HEADERS（记录请求和应答的头的基本信息）
         *   FULL（记录请求和响应的头信息，正文和元数据）
         */
        return Logger.Level.FULL;
 }
```

![FeignLog](https://img-blog.csdnimg.cn/direct/e6b180210f6b4a2aae80b30fe393c839.png)

## 3. 超时时间

[Feign Client的各种超时时间设置](https://blog.csdn.net/agonie201218/article/details/118802928)
