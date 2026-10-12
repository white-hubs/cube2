package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import java.util.Set;

@Data
public class DeviceProductCountQueryParam {
    private Set<Long> productIds;
    private Set<Long> deviceIds;
}
