package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

import lombok.Data;

import java.util.List;

/**
 * @author mioopp
 * @create 2021-11-26 15:42
 */
@Data
public class RuleTriggerLog {

  private String ruleId;

  private String ruleNum;

  private String instanceId;

  private String timestamp;

  private String delayEventTime;

  private String notifyStatus;

  private String notifyStatusTs;

  private List<CommonTriggerPart> ruleTriggerSnapshot;

  /**
   * 暂不被支持
   */
  private List<CommonTriggerPart> matchSnapshot;
}
