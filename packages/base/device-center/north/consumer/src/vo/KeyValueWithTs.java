package com.guhecloud.cube.iot.north.bridge.consumer.vo;

import lombok.Data;

/**
 * @author mioopp
 * @create 2021-11-20 10:01
 */
@Data
public class KeyValueWithTs {

  private String key;

  private Object value;

  private Object preValue;

  private boolean changed;//标志是否变化

  private Long ts;
}
