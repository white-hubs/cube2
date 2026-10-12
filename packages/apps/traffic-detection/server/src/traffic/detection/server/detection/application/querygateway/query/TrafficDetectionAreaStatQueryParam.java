package com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query;

import com.guhecloud.cube.biz.traffic.detection.server.common.query.StatisticsQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrafficDetectionAreaStatQueryParam extends StatisticsQuery {

    /**
     * 流量检测区域id集合
     */
    private Set<Long> trafficDetectionAreaIds;

    /**
     * 统计颗粒度：HOUR、DAY、MONTH、YEAR
     */
    private String granularity;

    /**
     * 排序字段：TOTAL 累计人次，AVG 均人次
     */
    private String sortField;
}
