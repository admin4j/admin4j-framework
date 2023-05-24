# Elasticsearch

## Usage

```xml

<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>elasticsearch-spring-boot-starter</artifactId>
    <version>0.2.0</version>
</dependency>
```

### 使用自己的es版本(向下兼容)

```xml

<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>elasticsearch-spring-boot-starter</artifactId>
    <version>0.2.0</version>
    <exclusions>
        <exclusion>
            <artifactId>elasticsearch-java</artifactId>
            <groupId>co.elastic.clients</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
<groupId>co.elastic.clients</groupId>
<artifactId>elasticsearch-java</artifactId>
<version>${elasticsearch.version}</version>
<exclusions>
    <exclusion>
        <artifactId>jakarta.json-api</artifactId>
        <groupId>jakarta.json</groupId>
    </exclusion>
</exclusions>
</dependency>
```

## 配置

```yaml
spring:
  elasticsearch:
    uris:
      - http://192.168.1.13:9200

## 开启访问日志
logging:
  level:
    org.springframework.data.elasticsearch.client.WIRE: TRACE
```

## elasticsearch-java 使用指南

[https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/introduction.html](https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/introduction.html)