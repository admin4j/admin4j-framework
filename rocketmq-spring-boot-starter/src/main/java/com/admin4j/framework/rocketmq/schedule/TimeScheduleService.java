package com.admin4j.framework.rocketmq.schedule;

/**
 * @author andanyang
 * @since 2024/1/25 18:11
 */
public interface TimeScheduleService {

    /**
     * 延迟调度
     *
     * @param bizCode
     * @param bizId
     * @param scheduleTimestamp
     */
    void delaySchedule(String bizCode, Object bizId, long scheduleTimestamp);
}
