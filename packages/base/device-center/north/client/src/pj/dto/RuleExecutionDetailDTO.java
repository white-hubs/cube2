package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

/**
 * 执行日志详细信息
 */
@Data
public class RuleExecutionDetailDTO {
    private String ruleId;
    private String ruleNum;
    private String instanceId;
    private String timestamp;
    private String notifyStatus;
    private String notifyStatusTs;

    private List<ActionStatusDTO> actionStatusDTOS;
    private List<RuleTriggerSnapshotDTO> ruleTriggerSnapshot;

}
