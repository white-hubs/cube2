package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则通知配置变更统计
 *
 * @author lanchun
 * @date 2024/9/14 14:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRuleNotifyConfigUpdateMessage {

    public static final String TOPIC = "MEDICAL_RULE_NOTIFY_CONFIG_UPDATE";

    /**
     * 时间戳
     */
    private Long timestamp;
}
