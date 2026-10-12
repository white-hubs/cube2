package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盘点任务-资产详情中间表
 *
 * @author : lijie
 * @date : 2023-3-20
 */
@TableName("property_check_task_relation")
@Data
public class PropertyCheckTaskRelation extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 资产id
     */
    private Long propertyId;
    /**
     * 盘点计划id
     */
    private Long planId;
    /**
     * 盘点任务id
     */
    private Long taskId;
    /**
     * 盘点状态
     */
    private String checkStatus;
    private LocalDateTime checkTime;
    private Long abnormalType;
    private String abnormalRemarks;
    private String abnormalFile;
}
