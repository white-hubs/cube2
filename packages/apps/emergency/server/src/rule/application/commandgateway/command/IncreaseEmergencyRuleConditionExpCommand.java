package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command;

import com.guhecloud.cube.biz.emergency.server.rule.domain.eumeration.CompareOpEnum;
import lombok.Data;

@Data
public class IncreaseEmergencyRuleConditionExpCommand {

    /**
     * 预案条件主键
     */
    private Long emergencyRuleConditionId;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 属性单位
     */
    private String propUnit;

    /**
     * 比较符（= >=）
     */
    private CompareOpEnum op;

    /**
     * 属性值
     */
    private String oprand;

    /**
     * 延时时间
     */
    private Integer delayTime;

    /**
     * 延迟开始时间
     */
    private Integer startDelayTime;

    /**
     * 延迟结束时间
     */
    private Integer endDelayTime;
}
