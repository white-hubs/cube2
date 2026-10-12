package com.guhecloud.cube.iot.mgt.core.data.vo.linkage;

import lombok.Data;

import java.util.List;

/**
 * 执行日志详细信息
 */
@Data
public class RuleExecutionDetail {
    private String ruleId;
    private String ruleNum;
    private String instanceId;
    private String timestamp;
    private String notifyStatus;
    private String notifyStatusTs;

    private List<ActionStatus> actionStatus;
    private List<RuleTriggerSnapshot> ruleTriggerSnapshot;

}
