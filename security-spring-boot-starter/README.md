# admin security

Spring Security 最佳实践封装。

# Features

- 多渠道登录
- 匿名url访问：一个注解/一个配置，解决匿名url访问（忽略认证）
- 注解式权限
- 基于数据库的动态权限

# USAGES

## 1. 基础使用

### 1.1 引入 pom

```xml
<!--https://central.sonatype.com/artifact/com.admin4j.framework/security-spring-boot-starter-->
<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>security-spring-boot-starter</artifactId>
    <version>0.9.5</version>
</dependency>
```

### 1.2 实现 JwtUserDetailsService 接口,用于根据用户ID获取用户详情。由于我们的JWT Token 存的是 userId,所以这里的入参为userId

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

### 1.3  账号密码登录。需要实现 `Spring Security`的`UserDetailsService` 接口，用于根据 username 查询用户详情

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

### 1.4 测试

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

## 2. 匿名url访问

### 2.1 注解式

> `@AnonymousAccess`需要放在 `controller`方法上

```
public class UserProfileController {

    @GetMapping("1")
    @ApiModelProperty("注解式匿名url访问")
    @AnonymousAccess
    public R<String> get() {
        return R.ok("1");
    }
```

### 2.2 yml配置式

> 支持HttpMethod(get,post,put,delete)配置；uris下面表示所有HttpMethod

```
admin4j:
  security:
    ignoring:
      uris:
        - "/login/sendPhoneCode"
        - "/profile/**"
      get:
        - "/profile/3"
```

## 3. 注解式权限

### 3.1 开启方法注解式权限

```
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminServerApplication {
}
```

### 3.2 使用

```
 	@GetMapping("4")
    @PreAuthorize("hasAuthority('profile')")
    public R<SysUserLoginInfoVO> get4() {
     return R.ok("4");
    }

    @GetMapping("5")
    @PreAuthorize("hasAuthority('menus')")
    public R<String> get5() {
          return R.ok("5");
    }
```

### 3.3 `@PreAuthorize` 支持el表达式

#### 3.3.1. returnObject 保留名

对于 @PostAuthorize 和 @PostFilter 注解, 可以在表达式中使用 returnObject 保留名, returnObject 代表着被注解方法的返回值,
我们可以使用 returnObject 保留名对注解方法的结果进行验证.
比如:

```java

@PostAuthorize("returnObject.owner == authentication.name")
public Book getBook();
12
```

#### 3.3.2. 表达式中的 # 号

在表达式中, 可以使用 #argument123 的形式来代表注解方法中的参数 argument123.
比如:

```java

@PreAuthorize("#book.owner == authentication.name")
public void deleteBook(Book book);
12
```

还有一种 #argument123 的写法, 即使用 Spring Security @P注解来为方法参数起别名, 然后在 @PreAuthorize 等注解表达式中使用该别名.
不推荐这种写法, 代码可读性较差.

```java

@PreAuthorize("#c.name == authentication.name")
public void doSomething(@P("c") Contact contact);
```

#### 3.3.3 内置表达式有:

| 表达式                                                                | 备注                                     |
|--------------------------------------------------------------------|----------------------------------------|
| hasRole([role])                                                    | 如果有当前角色, 则返回 true(会自动加上 ROLE_ 前缀)      |
| hasAnyRole([role1, role2])                                         | 如果有任一角色即可通过校验, 返回true,(会自动加上 ROLE_ 前缀) |
| hasAuthority([authority])                                          | 如果有指定权限, 则返回 true                      |
| hasAnyAuthority([authority1, authority2])                          | 如果有任一指定权限, 则返回true                     |
| principal                                                          | 获取当前用户的 principal 主体对象                 |
| authentication                                                     | 获取当前用户的 authentication 对象,             |
| permitAll                                                          | 总是返回 true, 表示全部允许                      |
| denyAll                                                            | 总是返回 false, 代表全部拒绝                     |
| isAnonymous()                                                      | 如果是匿名访问, 返回true                        |
| isRememberMe()                                                     | 如果是remember-me 自动认证, 则返回 true          |
| isAuthenticated()                                                  | 如果不是匿名访问, 则返回true                      |
| isFullAuthenticated()                                              | 如果不是匿名访问或remember-me认证登陆, 则返回true      |
| hasPermission(Object target, Object permission)                    |                                        |
| hasPermission(Object target, String targetType, Object permission) |                                        |

## 4. 基于数据库的动态权限

实现 接口`IPermissionUrlService`

```
public interface IPermissionUrlService {

    /**
     * 是否忽略 检查权限
     * 例如 admin、管理员可以直接忽略检查拥有全部权限
     *
     * @return
     */
    default boolean ignoreCheck() {
        return false;
    }

    /**
     * 是否允许匿名访问
     *
     * @return
     */
    default boolean canAnonymousAccess() {
        return false;
    }

    /**
     * 获取系统所有需要授权的 PermissionUri
     *
     * @return
     */
    List<HttpUrlPermission> allPermissionUrl();

    /**
     * 当前用户拥有的权限
     *
     * @return
     */
    List<HttpUrlPermission> getMyPermissionUrls();
}
```

## 5.多渠道登录

通过配置的方式，支持微信，手机号等多渠道登录

### 5.1 验证码手机号登录

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

