package com.admin4j.framework.rocketmq.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author andanyang
 * @since 2024/1/25 18:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelayScheduleDTO<T> {
    String bizCode;
    T bizId;
    long scheduleTimestampMS;
}
