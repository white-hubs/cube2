package com.guhecloud.cube.iot.mgt.device.param;

import lombok.Data;

import java.util.List;

@Data
public class DevicePropHistoryParam {
    private String prodCategoryId;
    private Long deviceId;
    private List<String> propCodes;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String startTime;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private String endTime;
    private String order = "desc";
    /**
     * 默认最大1000条,避免一次性查询过多数据
     */
    private Integer maxRecords = 1000;
}
