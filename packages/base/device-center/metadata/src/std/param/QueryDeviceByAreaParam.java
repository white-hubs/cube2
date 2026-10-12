package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

@Data
public class QueryDeviceByAreaParam {

    /**
     * 左匹配区域主键
     */
    private Long startWithAreaId;

    /**
     * 左匹配区域序列号
     */
    private String startWithAreaSeq;
}
