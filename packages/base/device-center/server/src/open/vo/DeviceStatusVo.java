package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

@Data
public class DeviceStatusVo {
    private String applicationName;
    private String applicationCode;
    private Integer offLineNum;
    private Integer normalNum;
}
