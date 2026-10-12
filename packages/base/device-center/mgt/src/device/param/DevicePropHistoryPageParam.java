package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DevicePropHistoryPageParam extends DevicePropHistoryParam {
    private Integer pageNumber;
    private Integer pageSize;
}
