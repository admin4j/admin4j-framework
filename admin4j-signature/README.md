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
         
         4、RequestParam请求参数：
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
             "body": "{"password":"1","user":{"1":"xx","2":"xx","area":"浦东新区","data":{"1":"xx","100":"xx","2":"xx","3":"xx","99":"xx"},"name":"张三","province":"上海市","sex":"男"},"username":"xiaoming"}"
         }
         appId：应用ID
         uri：可能包含动态参数，该字段只需参与签名，无需传递；签名忽略此字段：@Signature(uri = @SignatureField(enable = false))
         timestamp：时间戳
         nonce：随机数，可以使用UUID或者雪花ID，必须10位以上，否则验签失败
         body：如果body嵌套多层JSON对象或JSON数组如：{id: 1, name: "张三", "address": {"province": "上海市", "area": "浦东新区"}}
               那么内层对象的key也应该有顺序，需要递归处理所有JSON对象的key，最后直接把body对象转为对象字符串
               {
                  "body": "{"password":"1","user":{"1":"xx","2":"xx","area":"浦东新区","data":{"1":"xx","100":"xx","2":"xx","3":"xx","99":"xx"},"name":"张三","province":"上海市","sex":"男"},"username":"xiaoming"}"
               }
               如果body是JSON数组对象，则每个JSON对象中的key同样需要有排序，同上逻辑，最后把转JSON数组为数组字符串
               {
                   "body": "[{"1":"xx","2":"xx","3":"xx","area":"浦东新区","province":"上海市"},{"1":"xx","2":"xx","3":"xx","area":"徐汇区","province":"上海市"}]"
               }
               如果数组的元素非对象，则直接转为JSON字符串
               {
                 "body": "["3","2","1"]"
               }
         
         2、签名：
         sign：上面所有字段按key排序后keyvalue拼接，最后再拼接appSecret进行签名：md5("appIdzs001body{"password":"1","user":{"1":"xx","2":"xx","area":"浦东新区","data":{"1":"xx","100":"xx","2":"xx","3":"xx","99":"xx"},"name":"张三","province":"上海市","sex":"男"},"username":"xiaoming"}nonce1731062642266603520timestamp1701552470162uri/registerd3cbeed9baf4e68673a1f69a2445359a20022b7c28ea2933dd9db9f3a29f902b")
         "sign": "828a98e3c52e6c0c6a015fc99715e6e3"
         
         3、RequestHeader请求参数：
            appId=327189637812637
            timestamp=1701521859352
            nonce=1730934251223891968
            sign=1b695558ae9932f1d4544ba2bc6b6c05
         
         4、RequestBody请求参数：
            {
                "password": "1",
                "user": {
                    "area": "浦东新区",
                    "1": "xx",
                    "2": "xx",
                    "province": "上海市",
                    "data": {
                        "99": "xx",
                        "1": "xx",
                        "100": "xx",
                        "2": "xx",
                        "3": "xx"
                    },
                    "sex": "男",
                    "name": "张三"
                },
                "username": "xiaoming"
            }
         */
        return R.ok(user);
    }
}
```

## 重写com.admin4j.framework.signature.core.AbstractSignatureStrategy实现自定义签名规则


