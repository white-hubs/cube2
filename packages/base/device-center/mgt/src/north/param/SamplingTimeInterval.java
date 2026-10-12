package com.guhecloud.cube.iot.mgt.north.param;

import lombok.Getter;

@Getter
public enum SamplingTimeInterval {
    /**
     * 10分钟
     */
    INTERVAL_OF_10_MINUTE_ANY(10);

    private final long minute;

    SamplingTimeInterval(long minute) {
        this.minute = minute;
    }
}
