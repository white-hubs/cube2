package com.guhecloud.cube.property.management.client.property.param.property;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PropertyCheckPlanParam {
    /**
     * 盘点计划名称
     */
    @NotEmpty(message = "盘点计划名称不能为空")
    private String taskName;
    /**
     * 盘点责任人id
     */
    @NotNull(message = "盘点责任人不能为空")
    private Long dutyUserId;
    /**
     * 盘点方式
     */
    @NotEmpty(message = "盘点方式不能为空")
    private String checkType;
    /**
     * 是否允许手动选择
     */
    @NotNull(message = "是否允许手动选择不能为空")
    private Integer chooseType;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 预计完成时间
     */
    private LocalDateTime estimateEndTime;
    /**
     * 数据过滤条件
     */
    private PropertyCheckTaskDataSourceParam taskDataSource;

}
