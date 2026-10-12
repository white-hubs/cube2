package com.guhecloud.cube.iot.north.bridge.consumer.vo;

import lombok.Data;

import java.util.List;

/**
 * @author mioopp
 * @create 2021-11-26 15:42
 */
@Data
public class RuleTriggerMsg {

  private String ruleId;

  private String ruleNum;

  private String instanceId;

  private String timestamp;

  private String delayEventTime;

  private String notifyStatus;

  private String notifyStatusTs;

  private List<CommonTriggerPart> ruleTriggerSnapshot;

  private List<CommonTriggerPart> matchSnapshot;
}
