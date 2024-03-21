package com.admin4j.framework.rocketmq.util;

import com.admin4j.framework.AppTest;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author andanyang
 * @since 2024/1/25 18:29
 */
@SpringBootTest(classes = AppTest.class)
class RocketMqUtilTest {

    @Test
    void send() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {

        RocketMqUtil.send("RocketMqUtil-Topic", "RocketMqUtil-tags", "RocketMqUtil-keys", "RocketMqUtil-RocketMqUtil-body");
    }

    @Test
    void sendDelayTimeMs() {
    }
}