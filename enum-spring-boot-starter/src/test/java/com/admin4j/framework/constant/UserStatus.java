package com.admin4j.framework.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author andanyang
 * @since 2023/11/1 9:12
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    NORMAL(1, "正常"),
    FREEZE(2, "冻结");

    @EnumValue
    @JsonValue
    private final int value;
    private final String text;

    @JsonCreator
    public static UserStatus valueOf(int value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
