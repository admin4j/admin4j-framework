package com.admin4j.common.pojo.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局错误事件
 *
 * @author andanyang
 * @since 2020/5/12 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalExceptionEvent {
    /**
     * 异常类型
     */
    private String name;
    private Throwable e;


    public GlobalExceptionEvent(Exception e) {
        this.e = e;
    }
}
