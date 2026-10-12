package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 盘点任务
 *
 * @author : lijie
 * @date : 2023-3-20
 */
@Data
@TableName("property_check_task")
public class PropertyCheckTask extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;
    /**
     * 任务id
     */
    private Long planId;
    /**
     * 资产状态（已盘、盘亏、盘盈、未盘）
     */
    private String status;
    /**
     * 盘点计划名称
     */
    private String planName;
    /**
     * 盘点责任人id
     */
    private Long checkUserId;
    /**
     * 盘点资产数
     */
    private Long checkTotalNumber;
    /**
     * 已盘点
     */
    private Integer checkCompleteNumber;
    /**
     * 盘赢
     */
    private Integer checkWinNumber;
    /**
     * 盘亏
     */
    private Integer checkLossNumber;
    /**
     * 预计完成时间
     */
    private LocalDateTime estimateEndTime;
    /**
     * 实际完成时间
     */
    private LocalDateTime realityEndTime;
    private Integer abnormalNumber = 0;
}
