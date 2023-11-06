# desensitize-spring-boot-starter

接口数据脱敏(目前仅支持 jackson )。支持用户名，手机号，身份证号，邮件，地址等

## USAGE
```
<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>desensitize-spring-boot-starter</artifactId>
    <version>0.8.0</version>
</dependency>
```
最新版查询 [https://central.sonatype.com/artifact/com.admin4j.framework/desensitize-spring-boot-starter](https://central.sonatype.com/artifact/com.admin4j.framework/desensitize-spring-boot-starter)

```java
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    @Sensitivity(strategy = SensitivityEnum.EMAIL)
    private String email;

    /**
     * 手机号码
     */
    @Sensitivity(strategy = SensitivityEnum.PHONE)
    private String mobile;
}
```
