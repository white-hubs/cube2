package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 计划扩展信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_plan_expand")
@EqualsAndHashCode(callSuper = true)
public class DailyPlanExpandPO extends BasicEnhancedEntity<Long> {
    private Long id;

    /**
     * 附加字段数据
     */
    private String metadata;
}
