package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

import java.util.List;

/**
 * @author mioopp
 * @create 2021-11-26 15:42
 */
@Data
public class RuleTriggerLogDTO {

  private String ruleId;

  private String ruleNum;

  private String instanceId;

  private String timestamp;

  private String delayEventTime;

  private String notifyStatus;

  private String notifyStatusTs;

  private List<CommonTriggerPartDTO> ruleTriggerSnapshot;

  /**
   * 暂不被支持
   */
  private List<CommonTriggerPartDTO> matchSnapshot;
}
