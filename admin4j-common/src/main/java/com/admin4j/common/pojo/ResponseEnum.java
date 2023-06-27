package com.admin4j.common.pojo;


import com.admin4j.common.assertion.Assert;
import com.admin4j.common.assertion.AssertException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author andanyang
 * @since 2022/8/4 13:30
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum implements IResponse, Assert {

    /**
     * 操作成功
     */
    SUCCESS(200, "success"),

    /**
     * 业务异常
     */
    FAILURE(400, "业务异常"),

    /**
     * token 认证失败
     */
    FAIL_AUTH_TOKEN(401, "FAIL_AUTH_TOKEN"),
    /**
     * 登录失败，账号或者密码错误
     */
    FAIL_AUTH(402, "登录失败，账号或者密码错误"),
    /**
     *
     */
    FAIL_AUTH_FORBIDDEN(403, "FAIL_AUTH_FORBIDDEN"),


    /**
     * token 认证失败
     */
    FAIL_NO_TOKEN(405, "FAIL_NO_TOKEN"),


    /**
     * 服务未找到
     */
    NOT_FOUND(404, "The uri is not found"),

    /**
     * Too Many Requests
     */
    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    /**
     * 验证失败
     */
    VERIFY_ERROR(4000, "VERIFY_ERROR"),

    /**
     * 断言错误
     */
    ASSERT_ERROR(4001, "服务器内部错误 4001"),


    /**
     * 服务异常
     */
    ERROR(5000, "服务器内部错误"),


    THIRD_PARTY_API_ERROR(5001, "调用第三方接口失败"),
    DEMO_MODE_EXCEPTION(5002, "演示模式，不允许操作"),
    METHOD_ARGUMENT_NOTVALID_EXCEPTION(5003, "表单字段验证失败"),
    VALIDATED_BIND_EXCEPTION(5004, "接口字段绑定失败"),
    MAX_UPLOAD_SIZE_EXCEPTION(5005, "MAX_UPLOAD_SIZE_EXCEPTION"),
    ERROR_E(5006, "服务器内部错误 5006"),
    ERROR_ASSERT(5007, "服务器内部错误 5007"),

    SERVICE_API_ERROR(5100, "服务调用错误"),
    SERVICE_404(5101, "服务未找到"),
    SERVICE_FALLBACK(5102, "服务降级"),


    BIZ_ERROR(5200, "INTERNAL_SERVER_ERROR 5200"),

    /**
     * 发生SQL异常 SQLException
     */
    ERROR_SQL(5210, "INTERNAL_SERVER_ERROR: 5210"),
    /**
     * 发生运行时异常 RuntimeException
     */
    ERROR_RUNTIME(5220, "INTERNAL_SERVER_ERROR: 5220"),
    ERROR_ILLEGAL_ARGUMENT(5221, "INTERNAL_SERVER_ERROR: 5221"),

    /**
     * 空指针错误
     */
    ERROR_NULL(5240, "Server.internal.error code:5240"),

    /**
     * 分布式锁异常
     */
    ERROR_D_LOCK(5300, "服务器内部错误 5300"),
    ERROR_D_IDEMPOTENT(5301, "服务器内部错误 5301"),

    REQUEST_TOO_MANY_REQUESTS(5429, "TOO_MANY_REQUESTS"),
    USERNAME_NOT_FOUND(5400, "USERNAME_NOT_FOUND"),
    PASSWORD_ERROR(5401, "PASSWORD_ERROR"),
    /**
     * 账号或者密码不嫩为空
     */
    UN_PWD_NOT_NULL(5402, "UN_PWD_NOT_NULL"),
    /**
     * 账号过期
     */
    ACCOUNT_EXPIRED(5403, "ACCOUNT_EXPIRED"),
    /**
     * 账号不可用
     */
    ACCOUNT_DIS_ENABLED(5404, "ACCOUNT_DIS_ENABLED"),
    /**
     * 账号锁住
     */
    ACCOUNT_LOCKED(5405, "ACCOUNT_LOCKED"),
    /**
     * 密码过期
     */
    CREDENTIALS_EXPIRED(5406, "ACCOUNT_LOCKED"),


    /**
     * 获取当前用户失败
     */
    CURRENT_USER_FAIL(10001, "获取当前用户失败");

    /**
     * 状态码
     */
    final int code;
    /**
     * 消息内容
     */
    final String msg;

    /**
     * 创建异常
     *
     * @param t      异常
     * @param errMsg msg
     * @return
     */
    @Override
    public AssertException newException(Throwable t, String errMsg) {
        return new AssertException(this, t, errMsg);
    }

    @Override
    public AssertException newException(Throwable t) {
        return new AssertException(this, t);
    }

    @Override
    public AssertException newException(String errMsg) {
        return new AssertException(this, errMsg);
    }
}
