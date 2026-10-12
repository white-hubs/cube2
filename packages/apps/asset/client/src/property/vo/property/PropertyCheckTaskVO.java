package com.guhecloud.cube.property.management.client.property.vo.property;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskDataSourceParam;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PropertyCheckTaskVO {
    private Long id;
    /**
     * 盘点计划名称
     */
    private String taskName;
    /**
     * 盘点责任人id
     */
    private String dutyUserId;
    /**
     * 盘点方式
     */
    private String checkType;
    /**
     * 是否允许手动选择
     */
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
     * 实际完成时间
     */
    private LocalDateTime realityEndTime;
    /**
     * 数据过滤条件
     */
    private String dataSource;
    /**
     * 状态
     */
    private String status;
    /**
     * 任务资产数
     */
    private Integer propertyNumber;
    private PropertyCheckTaskDataSourceParam taskDataSource;

}
