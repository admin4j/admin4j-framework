package com.admin4j.framework.rocketmq.util;

import com.admin4j.spring.util.SpringUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author andanyang
 * @since 2024/1/25 16:46
 */
public class RocketMqUtil {

    private static RocketMQTemplate rocketMQTemplate;

    private static RocketMQTemplate getRocketMQTemplate() {
        if (Objects.isNull(rocketMQTemplate)) {
            rocketMQTemplate = SpringUtils.getBean(RocketMQTemplate.class);
        }
        return rocketMQTemplate;
    }

    static byte[] switchByte(Object body) {
        if (ObjectUtils.isNotEmpty(body)) {
            if (body instanceof String) {
                return ((String) body).getBytes(StandardCharsets.UTF_8);
            } else {
                // if (body instanceof ITenantModel) {
                //     ((ITenantModel) body).setTenantId(SecurityUtils.getTenantId());
                //     ((ITenantModel) body).setUserId(SecurityUtils.getUserId());
                // }
                return JSON.toJSONBytes(body);
            }
        }
        return new byte[0];
    }

    /**
     * 发送mq同步消息
     *
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @return
     */
    public static SendResult send(String topic, String tag, String key, Object body) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {

        return send(topic, tag, key, body, null);
    }

    /**
     * 发送毫秒级延迟消息
     *
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @param delayTimeMs 延迟时间戳（毫秒），到达 delayTimeMs 时触发消息
     * @return
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQClientException
     */
    public static SendResult sendDelayTimeMs(String topic, String tag, String key, Object body, long delayTimeMs) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {

        Message message = new Message();
        message.setTopic(topic);
        if (StringUtils.isNotEmpty(tag)) {
            message.setTags(tag);
        }
        if (StringUtils.isNotEmpty(key)) {
            message.setKeys(key);
        }
        message.setBody(switchByte(body));

        message.setDelayTimeMs(delayTimeMs);

        return send(message);
    }

    public static SendResult send(String topic, String tag, String key, Object body, Integer delayTimeLevel) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {

        Message message = new Message();
        message.setTopic(topic);
        if (StringUtils.isNotEmpty(tag)) {
            message.setTags(tag);
        }
        if (StringUtils.isNotEmpty(key)) {
            message.setKeys(key);
        }
        message.setBody(switchByte(body));
        if (delayTimeLevel != null) {
            message.setDelayTimeLevel(delayTimeLevel);
        }
        return send(message);
    }

    public static SendResult send(Message message) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        return getRocketMQTemplate().getProducer().send(message);
    }

    /**
     * 发送mq同步消息
     *
     * @param topic
     * @param body
     * @return
     */
    public static SendResult send(String topic, Object body) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {

        return send(topic, null, null, body);
    }

    /**
     * 发送mq异步消息
     *
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @return
     */
    public static void syncSend(String topic, String tag, String key, Object body, SendCallback sendCallback, long timeout) throws RemotingException, InterruptedException, MQClientException {
        syncSend(topic, tag, key, body, sendCallback, timeout, null);
    }

    public static void syncSend(String topic, String tag, String key, Object body, SendCallback sendCallback, long timeout, Integer delayTimeLevel) throws RemotingException, InterruptedException, MQClientException {

        Message message = new Message();
        message.setTopic(topic);
        if (StringUtils.isNotEmpty(tag)) {
            message.setTags(tag);
        }
        if (StringUtils.isNotEmpty(key)) {
            message.setKeys(key);
        }
        message.setBody(switchByte(body));
        if (delayTimeLevel != null) {
            message.setDelayTimeLevel(delayTimeLevel);
        }

        getRocketMQTemplate().getProducer().send(message, sendCallback, timeout);
    }

    /**
     * 发送mq异步消息
     *
     * @param topic
     * @param body
     * @return
     */
    public static void syncSend(String topic, Object body, SendCallback sendCallback, long timeout) throws RemotingException, InterruptedException, MQClientException {

        syncSend(topic, null, null, body, sendCallback, timeout);
    }

    /**
     * 发送mq一次性消息
     *
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @return
     */
    public static void sendOneWay(String topic, String tag, String key, Object body) throws RemotingException, InterruptedException, MQClientException {

        Message message = new Message();
        message.setTopic(topic);
        if (StringUtils.isNotEmpty(tag)) {
            message.setTags(tag);
        }
        if (StringUtils.isNotEmpty(key)) {
            message.setKeys(key);
        }
        message.setBody(switchByte(body));

        getRocketMQTemplate().getProducer().sendOneway(message);
    }

    /**
     * 发送mq一次性消息
     *
     * @param topic
     * @param body
     * @return
     */
    public static void sendOneWay(String topic, Object body) throws RemotingException, InterruptedException, MQClientException {

        sendOneWay(topic, null, null, body);
    }
}
