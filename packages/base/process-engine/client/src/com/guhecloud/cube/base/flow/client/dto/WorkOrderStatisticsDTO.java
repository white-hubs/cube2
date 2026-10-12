package com.guhecloud.cube.base.flow.client.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/18 20:06
 * @Description:
 */
@Data
public class WorkOrderStatisticsDTO {

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
