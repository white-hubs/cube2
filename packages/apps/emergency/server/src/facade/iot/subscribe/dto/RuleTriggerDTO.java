package com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto;

import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/7/6 17:48
 * @description
 */
@Data
public class RuleTriggerDTO {
    private String ruleId;
    private String projectId;
    private String ruleNum;
    private String instanceId;
    private String timestamp;
    private String notifyStatus;
    private String notifyStatusTs;
    private String delayEventTime;
    private List<SnapshotDTO> ruleTriggerSnapshot;
    private List<SnapshotDTO> matchSnapshot;
}
