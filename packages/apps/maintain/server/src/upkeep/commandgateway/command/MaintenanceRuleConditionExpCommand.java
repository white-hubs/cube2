package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-19  15:12
 * @Description:
 * @Version: 1.0
 */
@Data
public class MaintenanceRuleConditionExpCommand {
    /**
     * 预案主键
     */
    private Long emergencyRuleId;
    /**
     * 预案规则条件主键
     */
    private String emergencyRuleConditionId;
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
     * 属性操作符
     */
    private String op;
    /**
     * 属性值
     */
    private String oprand;
    /**
     * 延迟开始时间
     */
    private Integer startDelayTime;
    /**
     * 延迟时间
     */
    private Integer delayTime;
    /**
     * 延迟结束时间
     */
    private Integer endDelayTime;
    /**
     * 条件组主键
     */
    private Long groupId;
    private String conditionType;

}
