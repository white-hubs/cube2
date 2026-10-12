package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

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

  public static KeyValueWithTs toKeyValueWithTs(String key, Object value, Object preValue, boolean changed, Long ts) {
    KeyValueWithTs keyValueWithTs = new KeyValueWithTs();
    keyValueWithTs.setKey(key);
    keyValueWithTs.setValue(value);
    keyValueWithTs.setPreValue(preValue);
    keyValueWithTs.setChanged(changed);
    keyValueWithTs.setTs(ts);
    return keyValueWithTs;
  }
}
