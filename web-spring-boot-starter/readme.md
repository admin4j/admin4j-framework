# 参数校验

## 1、介绍

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

一个接口一般对参数（请求数据）都会进行安全校验，参数校验的重要性自然不必多说，那么如何对参数进行校验就有讲究了。一般来说有三种常见的校验方式，我们使用了最简洁的第三种方法

- 业务层校验
- Validator + BindResult 校验
- **Validator + 自动抛出异常**

业务层校验无需多说，即手动在 java 的 Service 层进行数据校验判断。不过这样太繁琐了，光校验代码就会有很多

而使用`Validator+ BindingResult`已经是非常方便实用的参数校验方式了，在实际开发中也有很多项目就是这么做的，不过这样还是不太方便，因为你每写一个接口都要添加一个
BindingResult 参数，然后再提取错误信息返回给前端（简单看一下）。

```
@PostMapping("/addUser")
public String addUser(@RequestBody @Validated User user, BindingResult bindingResult) {
    // 如果有参数校验失败，会将错误信息封装成对象组装在BindingResult里
    List<ObjectError> allErrors = bindingResult.getAllErrors();
    if(!allErrors.isEmpty()){
        return allErrors.stream()
            .map(o->o.getDefaultMessage())
            .collect(Collectors.toList()).toString();
    }
    // 返回默认的错误信息
    // return allErrors.get(0).getDefaultMessage();
    return validationService.addUser(user);
}
```

## 2、Validator + 自动抛出异常（使用）

内置参数校验如下：

| 注解               | 校验功能                  |
|------------------|-----------------------|
| @AssertFalse     | 必须是 false             |
| @AssertTrue      | 必须是 true              |
| @DecimalMax      | 小于等于给定的值              |
| @DecimalMin      | 大于等于给定的值              |
| @Digits          | 可设定最大整数位数和最大小数位数      |
| @Email           | 校验是否符合 Email 格式       |
| @Future          | 必须是将来的时间              |
| @FutureOrPresent | 当前或将来时间               |
| @Max             | 最大值                   |
| @Min             | 最小值                   |
| @Negative        | 负数（不包括 0）             |
| @NegativeOrZero  | 负数或 0                 |
| @NotBlank        | 不为 null 并且包含至少一个非空白字符 |
| @NotEmpty        | 不为 null 并且不为空         |
| @NotNull         | 不为 null               |
| @Null            | 为 null                |
| @Past            | 必须是过去的时间              |
| @PastOrPresent   | 必须是过去的时间，包含现在         |
| @PositiveOrZero  | 正数或 0                 |
| @Size            | 校验容器的元素个数             |

首先 Validator 可以非常方便的制定校验规则，并自动帮你完成校验。首先在入参里需要校验的字段加上注解,
每个注解对应不同的校验规则，并可制定校验失败后的信息：

```
@Data
public class User {
    @NotNull(message = "用户id不能为空")
    private Long id;

    @NotNull(message = "用户账号不能为空")
    @Size(min = 6, max = 11, message = "账号长度必须是6-11个字符")
    private String account;

    @NotNull(message = "用户密码不能为空")
    @Size(min = 6, max = 11, message = "密码长度必须是6-16个字符")
    private String password;

    @NotNull(message = "用户邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

校验规则和错误提示信息配置完毕后，接下来只需要在接口仅需要在校验的参数上加上 @Valid 注解 (去掉 BindingResult
后会自动引发异常，异常发生了自然而然就不会执行业务逻辑)：

```
@RestController
@RequestMapping("user")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @PostMapping("/addUser")
    public String addUser(@RequestBody @Validated User user) {

        return validationService.addUser(user);
    }
}
```

现在我们进行测试，打开 knife4j 文档地址，当输入的请求数据为空时，Validator
会将所有的报错信息全部进行返回，所以需要与全局[异常处理](https://so.csdn.net/so/search?q=异常处理&spm=1001.2101.3001.7020)
一起使用。

```
// 使用form data方式调用接口，校验异常抛出 BindException
// 使用 json 请求体调用接口，校验异常抛出 MethodArgumentNotValidException
// 单个参数校验异常抛出ConstraintViolationException
// 处理 json 请求体调用接口校验失败抛出的异常
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResultVO<String> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    List<String> collect = fieldErrors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
    return new ResultVO(ResultCode.VALIDATE_FAILED, collect);
}
// 使用form data方式调用接口，校验异常抛出 BindException
@ExceptionHandler(BindException.class)
public ResultVO<String> BindException(BindException e) {
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    List<String> collect = fieldErrors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
    return new ResultVO(ResultCode.VALIDATE_FAILED, collect);
}
```

![image-20230505100755096](http://md7.admin4j.com/blog/mysql/image-20230505100755096.png)

## 3、分组校验和递归校验

分组校验有三个步骤：

1. 定义一个分组类（或接口）
2. 在校验注解上添加`groups`属性指定分组
3. `Controller`方法的`@Validated`注解添加分组类

```
public interface Update extends Default{
}
```

```
@Data
public class User {
    @NotNull(message = "用户id不能为空",groups = Update.class)
    private Long id;
  ......
}
```

```
@PostMapping("update")
public String update(@Validated({Update.class}) User user) {
    return "success";
}
```

如果`Update`不继承`Default`，`@Validated({Update.class})`就只会校验属于`Update.class`
分组的参数字段；如果继承了，会校验了其他默认属于`Default.class`分组的字段。

对于递归校验（比如类中类），只要在相应属性类上增加`@Valid`注解即可实现（对于集合同样适用）

## 4、自定义校验

Spring Validation 允许用户自定义校验，实现很简单，分两步：

1. 自定义校验注解
2. 编写校验者类

```
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {HaveNoBlankValidator.class})// 标明由哪个类执行校验逻辑
public @interface HaveNoBlank {

    // 校验出错时默认返回的消息
    String message() default "字符串中不能含有空格";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    /**
     * 同一个元素上指定多个该注解时使用
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        NotBlank[] value();
    }
}
```

```
public class HaveNoBlankValidator implements ConstraintValidator<HaveNoBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 不做检验
        if (value == null) {
            return true;
        }
        // 校验失败
        return !value.contains(" ");
        // 校验成功
    }
}
```