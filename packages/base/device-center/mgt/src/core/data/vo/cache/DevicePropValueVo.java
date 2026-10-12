package com.guhecloud.cube.iot.mgt.core.data.vo.cache;

import lombok.Data;

@Data
public class DevicePropValueVo {
    /**
     * 上一次的值
     */
    private String preValue;
    /**
     * 当前值
     */
    private String value;
    /**
     * 时间戳
     */
    private long ts;
}
