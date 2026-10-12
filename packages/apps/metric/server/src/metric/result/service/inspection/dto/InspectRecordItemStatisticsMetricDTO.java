package com.guhecloud.cube.biz.metric.server.metric.result.service.inspection.dto;

import lombok.Data;

@Data
public class InspectRecordItemStatisticsMetricDTO {

    /**
     * 项id
     */
    private Long itemId;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 总数
     */
    private Integer totalNumber;
}
