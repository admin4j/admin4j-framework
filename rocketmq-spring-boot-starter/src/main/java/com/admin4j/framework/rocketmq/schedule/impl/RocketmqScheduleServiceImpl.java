package com.admin4j.framework.rocketmq.schedule.impl;

import com.admin4j.framework.rocketmq.schedule.TimeScheduleService;
import com.admin4j.framework.rocketmq.schedule.entity.DelayScheduleDTO;
import com.admin4j.framework.rocketmq.util.RocketMqUtil;

/**
 * @author andanyang
 * @since 2024/1/25 18:13
 */
public class RocketmqScheduleServiceImpl implements TimeScheduleService {

    private static final long MQ_MIN_SCHEDULE = 15 * 60 * 1000;
    private static final String MQ_DELAY_TOPIC = "Delay-Schedule";

    /**
     * 延迟调度
     *
     * @param bizCode
     * @param bizId
     * @param scheduleTimestamp
     */
    @Override
    public void delaySchedule(String bizCode, Object bizId, long scheduleTimestamp) {

        // 保存数据库

        long timeMillis = System.currentTimeMillis();
        if (scheduleTimestamp - timeMillis < MQ_MIN_SCHEDULE) {
// 直接发起 延迟消息
        }
    }

    // 定时任务调度
    public boolean sendSchedule(String bizCode, Object bizId, long scheduleTimestamp) {

        DelayScheduleDTO<?> delayScheduleDTO = new DelayScheduleDTO<>(bizCode, bizId, scheduleTimestamp);
        try {
            RocketMqUtil.sendDelayTimeMs(MQ_DELAY_TOPIC, null, bizCode, delayScheduleDTO, scheduleTimestamp);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 更新mq
    }
}
