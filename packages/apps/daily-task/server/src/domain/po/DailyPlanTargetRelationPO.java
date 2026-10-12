package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 计划的对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("daily_plan_target_relation")
public class DailyPlanTargetRelationPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long planId;

    /**
     * 分发规则ID
     */
    private Long ruleId;
    /**
     * 对象类别
     * (设备/档位/现场点/资产/风险源/小散工程)
     */
    private String targetCategory;
    private Long targetId;

    /**
     * 标签ID
     */
    private String tagIds;
}
