package com.admin4j.framework.tenant.autoconfigure;

import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.framework.tenant.rocketmq.MqSendMessageHook;
import com.admin4j.framework.tenant.rocketmq.RocketmqBeanPostProcessor;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author andanyang
 * @since 2023/9/20 9:05
 */
@ConditionalOnClass(name = "org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration")
@AutoConfigureAfter(RocketMQAutoConfiguration.class)
public class TRocketMQAutoConfiguration {

    @Bean
    // @ConditionalOnBean(value = {SendMessageHook.class, ConsumeMessageHook.class})
    public RocketmqBeanPostProcessor rocketmqBeanPostProcessor() {
        return new RocketmqBeanPostProcessor();
    }

    @Bean
    @ConditionalOnBean(IUserContextHolder.class)
    public SendMessageHook mqSendMessageHook(IUserContextHolder userContextHolder) {
        return new MqSendMessageHook(userContextHolder);
    }
}
