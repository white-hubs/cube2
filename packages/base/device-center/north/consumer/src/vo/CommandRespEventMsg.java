package com.guhecloud.cube.iot.north.bridge.consumer.vo;

import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-26 15:45
 */
@Data
public class CommandRespEventMsg {

  private String deviceId;

  private String propCode;

  private String propValue;

  private String propValueTs;

  private String condition;

}
