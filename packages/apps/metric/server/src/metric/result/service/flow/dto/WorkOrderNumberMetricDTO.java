package com.guhecloud.cube.biz.metric.server.metric.result.service.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderNumberMetricDTO {

    /**
     * 类型编码
     */
    private String businessType;
    /**
     * 工单类型
     */
    private String businessTypeName;
    /**
     * 总数
     */
    private Integer totalNumber;
    /**
     * 处理中工单数
     */
    private Integer processNumber;
    /**
     * 驳回工单数
     */
    private Integer rejectNumber;
    /**
     * 驳回工单数
     */
    private Integer undoNumber;

    /**
     * 驳回工单数
     */
    private Integer dealNumber;

}
