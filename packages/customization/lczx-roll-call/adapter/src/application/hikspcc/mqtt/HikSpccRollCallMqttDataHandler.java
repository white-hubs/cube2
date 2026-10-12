package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

/**
 * 协议处理
 */
@Slf4j
public class HikSpccRollCallMqttDataHandler {

    /**
     * 处理接收的消息体
     *
     * @param mqttMessage 接收到的消息体
     *
     * @return 解析后的json消息体
     */
    public static String messageResolve(MqttMessage mqttMessage) {
        byte[] payload = mqttMessage.getPayload();
        return new String(payload, StandardCharsets.UTF_8);

    }

    /**
     * 检查topic是否符合要求
     *
     * @param topic mqtt订阅的主题
     *
     * @return true or false
     */
    public static boolean checkTopic(String topic) {
        boolean b = true;
        String[] subTopics = topic.split("/");
        if (subTopics.length != 5) {
            b = false;
        }

        if (!"artemis".equals(subTopics[0]) && !"6140796929".equals(subTopics[2])) {
            b = false;
        }
        return b;
    }
}
