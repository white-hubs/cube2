package com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 开关屏配置管理 实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("information_plan")
@EqualsAndHashCode(callSuper = true)
public class InformationPlanEntity extends BasicEnhancedTenantEntity<Long> {
    /**
     * 计划主键id
     */
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 计划开始时间
     */
    private LocalDateTime planStartTime;
    /**
     * 计划结束时间
     */
    private LocalDateTime planEndTime;
    /**
     * 开屏时间（精确到分）
     */
    private String openTime;
    /**
     * 关屏时间（精确到分）
     */
    private String closeTime;
    /**
     * 是否启用 0：禁用 1：启用
     */
    private Integer isEnable;
    /**
     * 执行周期集合 1：周一 2：周二 3：周三 4：周四 5：周五 6：周六 7：周日 (逗号分割)
     */
    private String executionCycle;
    /**
     * 开屏的xxljob的任务id
     */
    private String openJobTaskId;
    /**
     * 关屏的xxljob的任务id
     */
    private String closeJobTaskId;
}
