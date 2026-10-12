package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRuleModifyMessage {

    public static final String TOPIC = "MEDICAL_RULE_MODIFY";

    /**
     * 规则主键
     */
    private Long id;

    /**
     * 时间戳
     */
    private Long timestamp;
}
