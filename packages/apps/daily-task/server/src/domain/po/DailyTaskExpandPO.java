package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务扩展信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task_expand")
@EqualsAndHashCode(callSuper = true)
public class DailyTaskExpandPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;

    /**
     * 附加字段数据
     */
    private String metadata;
}
