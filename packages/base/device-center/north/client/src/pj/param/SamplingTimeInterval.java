package com.guhecloud.cube.iot.north.bridge.client.pj.param;

import lombok.Getter;

@Getter
public enum SamplingTimeInterval {
    /**
     * 10分钟
     */
    INTERVAL_OF_10_MINUTE(10);

    private final long minute;

    SamplingTimeInterval(long minute) {
        this.minute = minute;
    }
}
