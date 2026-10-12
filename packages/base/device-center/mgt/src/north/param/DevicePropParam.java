package com.guhecloud.cube.iot.mgt.north.param;

import lombok.Data;

@Data
public class DevicePropParam {
    Long deviceId;
    String propertyCodes;
    Long notBeforeTs;
}
