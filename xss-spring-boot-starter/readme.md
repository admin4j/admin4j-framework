# 1. 什么是XSS介绍

**XSS**: Cross Site Scripting，为不和层叠样式表(Cascading Style Sheets, CSS) 的缩写混淆，故将跨站脚本攻击缩写为XSS。

恶意攻击者往Web页面里插入恶意Script代码，当用户浏览该页之时，嵌入其中 Web里面的Script代码会被执行，从而达到恶意攻击用户的目的。

在一开始的时候，这种攻击的演示案例是跨域的，所以叫"跨站脚本"。
但是发展到今天，由于JavaScript的强大功能基于网站前端应用的复杂化，是否跨域已经不再重要。但是由于历史原因，XSS这个名字一直保留下来。
XSS长期以来被列为客户端Web安全中的头号大敌。因为XSS破坏力强大，且产 生的场景复杂，难以一次性解决。
现在业内达成的共识是：针对各种不同场景产生的XSS，需要区分情景对待。
**攻击原理：**

XSS的原理是WEB应用程序混淆了用户提交的数据和JS脚本的代码边界，导致浏览器把用户的输入当成了JS代码来执行。

> 比如再评论接口，本来提交的是正常的字符串，但是坏人提交了`<script>alert('hey!you are attacked');</script>`
> 或者`<script>alert('hey!you are attacked');</script>`
> 。再别人看他发的评论的时候。如果是直接将评论渲染数来是不是就直接执行了这个脚本了，第一个劫持流量实现恶意跳转、第二个是恶意弹窗。当然还很多工具方式。参考：[万字讲解9种Web应用攻击与防护安全。XSS、CSRF、SQL注入等是如何实现的](https://blog.csdn.net/agonie201218/article/details/129871312)

# Springboot中防御

`xss-spring-boot-starter` 自动装配包封装了`AntiSamy`实现了XSS防御功能。

> AntiSamy是OWASP的一个开源项目，通过对用户输入的 `HTML / CSS / JavaScript`
> 等内容进行检验和清理，确保输入符合应用规范。AntiSamy被广泛应用于Web服务对存储型和反射型XSS的防御中。

maven坐标：

```
<dependency>
    <groupId>com.admin4j</groupId>
    <artifactId>xss-spring-boot-starter</artifactId>
    <version>0.0.7</version>
</dependency>
```

查看最新依赖版本 [https://central.sonatype.com/artifact/com.admin4j/xss-spring-boot-starter/](https://central.sonatype.com/artifact/com.admin4j/xss-spring-boot-starter)

## 配置

```
admin4j:
  xss:
    match-pattern: 1
    ignore-uris:
      - /xss/form-ignore/?
    include-uris:
      - /xss/form-include/?
```

- match-pattern: 匹配模式
    - 0：全部关闭(不使用xss)
    - 1 忽略模式 （按照 ignoreUris 没有出现在这里的才会执行xss过滤)
    - 2 包含模式 （按照 includeUris 只有出现在这里的才会执行xss过滤 )
    - 其他全部开启xss
- ignore-uris：忽略 uri 列表，支持ant风格
- include-uris：包含uri 列表，支持ant风格
- antisamy-policy: 防护xss策略。可选值："","ebay","anythinggoes","myspace","slashdot","tinymce", 自定义

### AntiSamy策略文件

ntiSamy对“恶意代码”的过滤依赖于策略文件。策略文件规定了AntiSamy对各个标签、属性的处理方法，策略文件定义的严格与否，决定了AntiSamy对XSS漏洞的防御效果。在AntiSamy的jar包中，包含了几个常用的策略文件，

![image-20230427173421250](http://md7.admin4j.com/blog/SpringCloud/image-20230427173421250.png)

此处我们拷贝antisamy-ebay.xml文件到resources目录下并命名为antisamy-test.xml，来自定义AntiSamy策略

![image-20230427173504681](http://md7.admin4j.com/blog/SpringCloud/image-20230427173504681.png)

## 测试

```
@RestController
@RequestMapping("xss")
public class XssController {

    @GetMapping
    public String get(String key) {

        return key;
    }

    @PostMapping("form")
    public String form(User user) {

        return JSON.toJSONString(user);
    }

    @PostMapping("form-ignore/**")
    public String formIgnore(User user) {

        return JSON.toJSONString(user);
    }

    @PostMapping("form-include/**")
    public String formInclude(User user) {

        return JSON.toJSONString(user);
    }

    @PostMapping("json")
    public String json(@RequestBody User user) {

        return JSON.toJSONString(user);
    }
}
```

测试代码地址 [https://github.com/admin4j/admin4j-example/blob/master/src/main/java/com/admin4j/xss/XssController.java](https://github.com/admin4j/admin4j-example/blob/master/src/main/java/com/admin4j/xss/XssController.java)

![image-20230427173534453](http://md7.admin4j.com/blog/SpringCloud/image-20230427173534453.png)

![image-20230427173552129](http://md7.admin4j.com/blog/SpringCloud/image-20230427173552129.png)

![image-20230427173629644](http://md7.admin4j.com/blog/SpringCloud/image-20230427173629644.png)

项目地址：[https://github.com/admin4j/admin4j-framework](https://github.com/admin4j/admin4j-framework)

