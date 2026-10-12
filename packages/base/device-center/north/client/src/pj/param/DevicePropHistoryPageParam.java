package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DevicePropHistoryPageParam extends DevicePropHistoryParam {
    private Integer page;
    private Integer pageSize;
}
