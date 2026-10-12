package com.guhecloud.cube.iot.mgt.north.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceSamplingPageParam extends DevicePropHistoryPageParam{
    /**
     * 采样间隔<br/>
     * default:  {@link SamplingTimeInterval#INTERVAL_OF_10_MINUTE_ANY}
     */
    private SamplingTimeInterval samplingTimeInterval;
}
