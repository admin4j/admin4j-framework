package com.admin4j.common.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局错误事件
 *
 * @author andanyang
 * @since 2022/5/12 17:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalExceptionEvent {
    private Exception e;
}
