package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.rocketmq.message;

import lombok.Data;

/**
 * 预案规则修改消息
 *
 * @author lanchun
 * @date 2024/2/21 13:45
 */
@Data
public class EmergencyChangeMessage {

    public static final String TOPIC = "emergency_change";

    private Long id;
}
