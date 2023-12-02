# signature-spring-boot-starter
`signature-spring-boot-starter` 通过注解的方式实现api接口签名

# 使用

```xml

<dependency>
    <groupId>com.admin4j.signature</groupId>
    <artifactId>admin4j-signature</artifactId>
    <version>0.8.0-SNAPSHOT</version>
</dependency>
```

## CODE

## 步骤1 实现SignatureApi接口
```java

/**
 * 签名API实现类
 */
@Service
@DubboService(version = "1.0.0")
public class SignatureApiImpl implements SignatureApi {

    @Override
    public String getAppSecret(String appId) {
        // 自定义逻辑获取appSecret
        return "d3cbeed9baf4e68673a1f69a2445359a20022b7c28ea2933dd9db9f3a29f902b";
    }

    @Override
    public String digestEncoder(String plainText) {
        // 签名加密算法，算法与前端保持一致
        try {
            return DigestUtils.md5DigestAsHex(StringUtils.getBytes(plainText, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
```

## 步骤2 具体需要签名的地方埋点@Signature，注释中包含前端参数加签规则

```java
@RestController
@RequestMapping("/user")
public class SignatureController {

    @GetMapping("/{id}")
    @Signature
    public R get(@PathVariable("id") Long id, String name) {
        /**
         1、参与加签字段
         {
             "appId": "327189637812637",
             "uri": "/user/get/1",
             "timestamp": 1701521859352,
             "nonce": "1730934251223891968",
             "param": "{\"name\": \"张三\"}",
         }
         appId：应用ID
         uri：可能包含动态参数，该字段只需参与签名，无需传递；签名忽略此字段：@Signature(uri = @SignatureField(enable = false))
         timestamp：时间戳
         nonce：随机数，可以使用UUID或者雪花ID，必须10位以上，否则验签失败
         param：url后的请求参数，封装为一个JSON对象，key排序，最后转为JSON字符串对象
         2、签名：
         sign：上面所有字段按key排序后keyvalue拼接，最后再拼接appSecret进行签名：md5("appId327189637812637nonce1730934251223891968param{"name":"张三"}timestamp1701521859352uri/user/get/1d3cbeed9baf4e68673a1f69a2445359a20022b7c28ea2933dd9db9f3a29f902b")
         3、RequestHeader请求参数：
           appId=327189637812637
           timestamp=1701521859352
           nonce=1730934251223891968
           sign=337337309c3e799c9a13c6ad9ddbf767
           RequestParam请求参数：
           name=张三
         */
        User user = new User(name, id, id == 1 ? "男" : "女");
        return R.ok(user);
    }

    @PostMapping
    @Signature
    public R create(@RequestBody User user) {
        /**
         1、前端加签参数, 需要按key排序
         {
             "appId": "327189637812637",
             "uri": "/user",
             "timestamp": 1701521859352,
             "nonce": "1730934251223891968",
             "body": "{"address":"{\"1\":\"xx\",\"2\":\"xx\",\"3\":\"xx\",\"area\":\"浦东新区\",\"province\":\"上海市\"}","name":"张三","sex":"男"}",
         }
         appId：应用ID
         uri：可能包含动态参数，该字段只需参与签名，无需传递；签名忽略此字段：@Signature(uri = @SignatureField(enable = false))
         timestamp：时间戳
         nonce：随机数，可以使用UUID或者雪花ID，必须10位以上，否则验签失败
         body：如果body是多层级的JSON对象如：{id: 1, name: "张三", "address": {"province": "上海市", "area": "浦东新区"}}
               那么内层对象的key也应该有顺序，最后直接把address转为对象字符串
               {
                  "body": "{"address":"{\"1\":\"xx\",\"2\":\"xx\",\"3\":\"xx\",\"area\":\"浦东新区\",\"province\":\"上海市\"}","name":"张三","sex":"男"}"
               }
               如果body是JSON数组对象，则每个对象中的key同样需要有顺序，最后转为数组字符串
               {
                   "body": "[{"1":"xx","2":"xx","3":"xx","area":"浦东新区","province":"上海市"},{"1":"xx","2":"xx","3":"xx","area":"徐汇区","province":"上海市"}]"
               }
               如果数组的元素非对象，则直接转为JSON字符串
               {
                 "body": "["3","2","1"]"
               }
         2、签名：
         sign：上面所有字段按key排序后keyvalue拼接，最后再拼接appSecret进行签名：md5("appId327189637812637body{"address":"{\"1\":\"xx\",\"2\":\"xx\",\"3\":\"xx\",\"area\":\"浦东新区\",\"province\":\"上海市\"}","name":"张三","sex":"男"}nonce1730934251223891968timestamp1701521859352uri/userd3cbeed9baf4e68673a1f69a2445359a20022b7c28ea2933dd9db9f3a29f902b")
         "sign": "828a98e3c52e6c0c6a015fc99715e6e3"
         3、RequestHeader请求参数：
            appId=327189637812637
            timestamp=1701521859352
            nonce=1730934251223891968
            sign=828a98e3c52e6c0c6a015fc99715e6e3
            RequestBody请求参数：
            {
                "name": "张三",
                "sex": "男",
                "address": {
                    "province": "上海市",
                    "area": "浦东新区",
                    "3": "xx",
                    "2": "xx",
                    "1": "xx"
                }
            }
         */
        return R.ok(user);
    }
}
```

## 重写com.admin4j.framework.signature.core.AbstractSignatureStrategy实现自定义签名规则


