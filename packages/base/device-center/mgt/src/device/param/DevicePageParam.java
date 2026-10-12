package com.guhecloud.cube.iot.mgt.device.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DevicePageParam extends Paging {
    private Long prodId;
    private Long prodCategoryId;
    private String deviceNameOrNumber;
    private Integer status;

    //是否为主设备
    private Boolean isMainDeviceOnly;
    //查询设备Ids,使用，分隔
    private String deviceIds;

}
