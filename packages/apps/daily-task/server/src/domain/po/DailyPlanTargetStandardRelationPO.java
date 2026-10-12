package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 计划对象的执行标准关联关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("daily_plan_target_standard_relation")
public class DailyPlanTargetStandardRelationPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long planId;
    /**
     * 对象类别
     * (设备/档位/现场点/资产/风险源/小散工程)
     */
    private String targetCategory;
    /**
     * 对象id
     */
    private Long targetId;
    /**
     * 对象执行标准id
     */
    private Long standardId;
}
