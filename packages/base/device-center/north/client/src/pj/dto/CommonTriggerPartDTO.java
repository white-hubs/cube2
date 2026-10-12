package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-26 15:45
 */
@Data
public class CommonTriggerPartDTO {

  private String deviceId;

  private String propCode;

  private String propValue;

  private String propValueTs;

  private String condition;

}
