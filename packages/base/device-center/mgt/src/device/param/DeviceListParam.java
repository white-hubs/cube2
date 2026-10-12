package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;


@Data
public class DeviceListParam {
    private Long prodId;
    private Long prodCategoryId;
    private String deviceNameOrNumber;
    private Integer status;
    //是否为主设备
    private Boolean isMainDeviceOnly;
    //查询设备Ids,使用，分隔
    private String deviceIds;
}
