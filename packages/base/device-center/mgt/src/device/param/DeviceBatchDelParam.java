package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;

import java.util.List;

@Data
public class DeviceBatchDelParam {
    private List<Long> ids;
}
