package com.admin4j.framework.tenant.rocketmq;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.List;

/**
 * @author andanyang
 * @since 2023/9/20 8:55
 */
public class RocketmqBeanPostProcessor implements BeanPostProcessor {

    @Autowired(required = false)
    private List<SendMessageHook> sendMessageHookList;
    @Autowired(required = false)
    private List<ConsumeMessageHook> consumeMessageHookList;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof DefaultMQProducer) {
            //注册生产者钩子
            if (ObjectUtils.isNotEmpty(sendMessageHookList)) {
                for (SendMessageHook sendMessageHook : sendMessageHookList) {
                    ((DefaultMQProducer) bean).getDefaultMQProducerImpl().registerSendMessageHook(sendMessageHook);
                }
            }
        }

        if (bean instanceof DefaultRocketMQListenerContainer) {
            //注册生产者钩子
            if (ObjectUtils.isNotEmpty(consumeMessageHookList)) {
                for (ConsumeMessageHook consumeMessageHook : consumeMessageHookList) {
                    ((DefaultRocketMQListenerContainer) bean).getConsumer().getDefaultMQPushConsumerImpl().registerConsumeMessageHook(consumeMessageHook);
                }
            }
        }
        return bean;
    }
}
