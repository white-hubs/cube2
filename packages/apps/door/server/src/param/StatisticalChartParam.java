package com.guhecloud.cube.biz.door.server.param;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class StatisticalChartParam {
    /**
     * 时间维度
     */
    private String dimension;
    /**
     * 状态
     */
    private String eventResult;
    private Long sceneType;
    private Long areaId;
    private List<Long> businessIds;
    private Set<String> deviceCodes;
    // 三方设备ids
    private List<String> thirdPartyIds;
}
