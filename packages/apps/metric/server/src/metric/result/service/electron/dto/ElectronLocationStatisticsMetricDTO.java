package com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto;

import lombok.Data;

/**
 * @author wql
 * @describe 巡更点统计
 * @date 2023/5/30 14:35
 */
@Data
public class ElectronLocationStatisticsMetricDTO {
    /**
     * 巡更点名称
     */
    private String locationName;
    /**
     * 巡更点id
     */
    private Long locationId;
    /**
     * 应巡次数
     */
    private Integer totalNum;
}
