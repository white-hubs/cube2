package com.guhecloud.cube.base.device.center.core.param;

import lombok.Data;

import java.util.Set;

@Data
public class CreateDeviceRefParam {
    private Set<Long> refDeviceIds;
}
