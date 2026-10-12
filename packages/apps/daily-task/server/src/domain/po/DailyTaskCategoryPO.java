package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task_category")
@EqualsAndHashCode(callSuper = true)
public class DailyTaskCategoryPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 任务类型名称
     */
    private String name;
    /**
     * 任务类型
     */
    private String dailyType;
}
