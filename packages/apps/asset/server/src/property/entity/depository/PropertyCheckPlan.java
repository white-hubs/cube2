package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盘点计划;
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Data
@TableName("property_check_plan")
public class PropertyCheckPlan extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 盘点计划名称
     */
    private String taskName;
    /**
     * 盘点责任人id
     */
    private Long dutyUserId;
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
}
