package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo;

import lombok.Data;

@Data
public class RiskWarningRuleVO {
    /**
     * 预警规则 ID。
     */
    private Long id;

    /**
     * 预警类型，取值：LEVEL_UPGRADE=等级升级预警，ASSESSMENT_OVERDUE=评估超期预警，
     * INDICATOR_EXCEED=指标超标预警。
     */
    private String warningType;

    /**
     * 预警规则条件 JSON。
     */
    private String conditionJson;

    /**
     * 预警严重程度。
     */
    private String severity;

    /**
     * 通知渠道。
     */
    private String notifyChannels;

    /**
     * 处置期限，单位为天。
     */
    private Integer disposeDeadlineDays;

    /**
     * 启用状态，true 表示启用，false 表示停用。
     */
    private Boolean enabled;
}
