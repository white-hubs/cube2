package com.guhecloud.cube.iot.mgt.north.param;

import lombok.Data;

import java.util.List;

@Data
public class DevicePropHistoryParam {
    private Long deviceId;
    private List<String> propCodes;
    private Long startTs;
    private Long endTs;

    private String order = "desc";
    /**
     * 默认最大1000条,避免一次性查询过多数据
     */
    private Integer maxRecords = 1000;
}
