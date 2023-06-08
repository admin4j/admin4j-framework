package com.admin4j.framework.desensitize.core;

import java.util.function.Function;

/**
 * 脱敏类型及策略，不同的字段类型适配不同的策略
 *
 * @author andanyang
 * @since 2023/6/8 13:07
 */
public enum SensitivityEnum {
    /**
     * 不返回
     */
    NONE(s -> ""),
    /**
     * 用户名
     */
    USERNAME(s -> s.replaceAll("\\S*(\\S)", "***$1")),
    /**
     * 身份证
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
    /**
     * 手机号
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),
    /**
     * 邮件
     */
    EMAIL(s -> s.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2")),
    /**
     * 地址
     */
    ADDRESS(s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2****"));


    private final Function<String, String> desensitizer;

    SensitivityEnum(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer() {
        return desensitizer;
    }
}
