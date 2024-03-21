package com.admin4j.framework;

import com.admin4j.framework.rocketmq.util.RocketMqUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Unit test for simple App.
 */
@SpringBootApplication
public class AppTest {

    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        SpringApplication.run(AppTest.class);

        send();
    }

 
    static void send() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {

        RocketMqUtil.send("RocketMqUtil-Topic", "RocketMqUtil-tags", "RocketMqUtil-keys", "RocketMqUtil-RocketMqUtil-body");
    }
}
