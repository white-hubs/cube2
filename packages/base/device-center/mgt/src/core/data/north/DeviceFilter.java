package com.guhecloud.cube.iot.mgt.core.data.north;

import lombok.Data;

import java.util.List;

/**
 * @author mioopp
 * @create 2021-11-23 19:19
 */
@Data
public class DeviceFilter {

  private String prodId;

  private String prodCode;

  private String prodCategory;

  private List<String> deviceIds;

}
