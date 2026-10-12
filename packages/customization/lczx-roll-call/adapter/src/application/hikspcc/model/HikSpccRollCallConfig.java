package com.guhecloud.cube.biz.adapter.person.rollcall.server.application.hikspcc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {
 * "host":"127.0.0.1:443",
 * "appKey":"",
 * "appSecret":"",
 * "tenant": 58197260,
 * "mqtthost": "tcp://mqttv2.forwayapp.com:1883",
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
public class HikSpccRollCallConfig {

    //平台的ip端口
    private String host;
    //密钥appkey
    private String appKey;
    //密钥appSecret
    private String appSecret;

    private Integer tenant;
    /**
     * 代理地址
     */
    private String mqttHost;

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
