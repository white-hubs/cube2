package com.guhecloud.cube.srv.gen.maintain.server.electron.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 计划的任务
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("patrol_plan_task")
@EqualsAndHashCode(callSuper = true)
public class PatrolPlanTask extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 时间范围
     */
    private String timeRange;
    /**
     * 间隔时间的值
     */
    private Integer intervalNumber;
    /**
     * 间隔时间单位
     */
    private Integer intervalUnit;
    /**
     * 拆分出来的实际时间段
     */
    private String executePeriod;
}
