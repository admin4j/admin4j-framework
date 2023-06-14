# log-spring-boot-starter

`log-spring-boot-starter` 通过注解的方式记录用户操作记录

# 使用

```xml

<dependency>
    <groupId>com.admin4j.framework</groupId>
    <artifactId>log-spring-boot-starter</artifactId>
    <version>0.6.0</version>
</dependency>
```

## CODE

## 步骤1 接受Spring 事件保存日志

```
    @EventListener
    @Async
    public void onSysLongEvent(SysLogEvent sysLogEvent) {

        String args = sysLogEvent.getArgs() == null ? "" : JSON.toJSONString(sysLogEvent.getArgs());
        baseMapper.save(sysLogEvent, args);
    }

```

## 步骤2 定义常量（非必须）

```java
public class SysLogConstant {

    public static final String USER_LOGIN_SUCCESS = "USER_LOGIN_SUCCESS";
    public static final String USER_LOGIN_SUCCESS_TYPE = "LOGIN";
    public static final String USER_ADD = "LOG_USER_ADD";
    public static final String USER_REMOVE = "LOG_USER_REMOVE";
    public static final String USER_UPDATE = "LOG_USER_UPDATE";
    public static final String USER_EXPORT = "LOG_USER_EXPORT";
    public static final String USER_IMPORT = "LOG_USER_IMPORT";
}
```

## 步骤3 具体需要记录日志的地方埋点

```java

@Api(tags = "用户")
public class SysUserController {

    @PutMapping
    @ApiOperation("新增")
    @SysLog(value = SysLogConstant.USER_REMOVE, args = {"#sysUserForm.userName"})
    public R create(@Validated(CreateGroup.class) @RequestBody SysUserForm sysUserForm) {


        sysUserService.create(sysUserForm);
        return R.okMsg("created successfully");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("删除")
    @SysLog(value = SysLogConstant.USER_REMOVE, args = {"#id"})
    public R delete(@PathVariable("id") long id) {
        sysUserService.removeById(id);
        return R.okMsg("deleted successfully");
    }

    @PostMapping("{id}")
    @ApiOperation("更新")
    @SysLog(value = SysLogConstant.USER_UPDATE, args = {"#id"})
    public R<Object> update(@PathVariable("id") long userId, @Validated @RequestBody SysUserForm sysUserForm) {
        sysUserService.updateByUserId(userId, sysUserForm);
        return R.okMsg("updated successfully");
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @SysLog(SysLogConstant.USER_EXPORT)
    public void exportExcel(UserQuery userQuery) throws IOException {

        sysUserService.exportExcel(userQuery);
    }

    @GetMapping("import")
    @ApiOperation("导入")
    @SysLog(SysLogConstant.USER_IMPORT)
    public void importExcel(MultipartFile file) throws IOException {

        sysUserService.importExcel(file);
    }
}
```