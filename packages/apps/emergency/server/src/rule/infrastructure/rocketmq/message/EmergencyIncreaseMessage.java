package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message;

import lombok.Data;

/**
 * 预案规则创建消息
 *
 * @author lanchun
 * @date 2023/12/26 14:50
 */
@Data
public class EmergencyIncreaseMessage {

    public static final String TOPIC = "emergency_increase";

    private Long id;
}
