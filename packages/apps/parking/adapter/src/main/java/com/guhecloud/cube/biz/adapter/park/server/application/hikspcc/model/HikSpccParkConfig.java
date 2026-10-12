package com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {
 * "tenant": 58197260,
 * "host": "tcp://mqttv2.forwayapp.com:1883",
 * "mqttLoginName": "your_mqtt_login_name",
 * "mqttPassword": "your_mqtt_password",
 * "mqttClientId": "mqtt-ghy-adapter-park",
 * "carEventVcheckTopic": "artemis/event_pcheck/454659/admin",
 * "keepAliveInterval": 30,
 * "connectTimeout": 60
 * }
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HikSpccParkConfig {

    private Integer tenant;
    /**
     * 代理地址
     */
    private String host;

    /**
     * MQTT登录
     */
    private String mqttLoginName;

    /**
     * MQTT密码
     */
    private String mqttPassword;

    /**
     * 客户端id  mqtt-ghy-adapter-park
     */
    private String mqttClientId;

    /**
     * 车辆核验事件 artemis/event_pcheck/454659/admin
     */
    private String carEventVcheckTopic;

    /**
     * 保持连接时间
     */
    private int keepAliveInterval = 30;

    /**
     * 连接超时时间
     */
    private int connectTimeout = 60;
}
