# admin security

## USAGES

1. 引入 pom

```
        <dependency>
            <groupId>com.admin4j.framework</groupId>
            <artifactId>security-spring-boot-starter</artifactId>
            <version>0.9.0</version>
        </dependency>
```

2. 实现 JwtUserDetailsService 接口,用于根据用户ID获取用户详情。由于我们的JWT Token 存的是 userId,所以这里的入参为userId

```java

@Component
public class Admin4jJwtUserDetailsService implements JwtUserDetailsService {

    @Autowired
    ISysUserLongInfoService sysUserLongInfoService;

    @Override
    public JwtUserDetails loadUserByUserId(Long userId) {

        return sysUserLongInfoService.getByUserId(userId);
    }
}
```

3. 账号密码登录。需要实现 `Spring Security`的`UserDetailsService` 接口，用于根据 username 查询用户详情

```java

@Component
public class Admin4jJwtUserDetailsService implements JwtUserDetailsService, UserDetailsService {

    @Autowired
    ISysUserLongInfoService sysUserLongInfoService;
    @Autowired
    ISysUserService sysUserService;

    @Override
    public JwtUserDetails loadUserByUserId(Long userId) {

        return sysUserLongInfoService.getByUserId(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUserName(username);
        return SysUserConvert.INSTANCE.convert(sysUser);
    }
}

```

### 测试

- 登录接口

```curl
curl --location 'http://localhost:8080/login' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=123456'
```

- 返回结果

```json
{
  "code": 200,
  "data": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzYWx0IjoiMTIiLCJleHAiOjE2OTkzNDU3ODAsInVzZXJJRCI6MX0.tz0RGKSQEwfS0aTrsF7bdxF1enU4Vy32rn4ckDn3-D0"
  },
  "msg": "success"
}
```

## 多渠道登录

通过配置的方式，支持微信，手机号等多渠道登录

### 验证码手机号登录

- yaml配置方式

```yaml
admin4j:
  security:
    ignoring:
      uris: "login/sendPhoneCode"
    multi:
      auth-map:
        phone: phoneNumber

```

auth-map 为渠道登录登录方式配置。对象类型，key为 登录方式 (`authType`)。value 为登录认证的字段名称。
解释如上配置，

- 登录方式 (`authType`) : `phone`
- 登录认证的字段名称: `phoneNumber`
- ignoring.uris 忽略认证的接口。需要将发送验证码接口，忽略认证。

前端登录接口案例：`/login/phone` (/login/authType)

```curl
curl --location 'http://localhost:8080/login/phone' \
--header 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'phoneNumber=admin' \
  --data-urlencode 'verificationCode=123456'
```

- java 配置代码，实现接口`MultiUserDetailsService`
    1. 配置登录方式 `support()` 为 phone （yaml配置的`authType`）
    2. 检查手机对应的验证码是否正确
    3. 根据手机号获取用户详情

```java

/**
 * 验证码手机号登录
 *
 */
@Component
public class PhoneMultiUserDetailsService implements MultiUserDetailsService {
    @Autowired
    ISysUserService sysUserService;

    @Override
    public String support() {
        return "phone";
    }

    @Override
    public boolean preVerify(MultiAuthenticationToken multiAuthenticationToken) {

        // 验证码
        String verificationCode = multiAuthenticationToken.getAuthParameter("verificationCode");
        // TODO 验证验证码正确性。错误返回 false,或者抛出错误
        return true;
    }

    @Override
    public UserDetails loadUserByMultiToken(String phoneNumber) {

        SysUser sysUser = sysUserService.getByPhoneNumber(phoneNumber);
        return SysUserConvert.INSTANCE.convert(sysUser);
    }
}

```

### 其他渠道登录，如微信openid 登录，参考上方可实现
