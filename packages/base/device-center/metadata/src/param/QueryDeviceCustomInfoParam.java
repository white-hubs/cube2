package com.guhecloud.cube.base.device.center.metadata.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryDeviceCustomInfoParam extends QueryDeviceSelfInfoParam {
    private boolean withCustomExt;
    private boolean withDeviceType;
}