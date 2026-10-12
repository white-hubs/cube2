package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PropertyCheckPlanPageVO extends PropertyCheckTaskEvolveVO {
    private Long id;
    /**
     * 状态
     */
    private String status;
    /**
     * 盘点任务名称
     */
    private String taskName;
    /**
     * 盘点计划资产总数
     */
    private Long propertyNumber;
    /**
     * 预计完成时间
     */
    private String estimateEndTime;
    /**
     * 时间完成时间
     */
    private LocalDateTime realityEndTime;
    /**
     * 盘点负责人
     */
    private String dutyUserName;
    private Long dutyUserId;
}
