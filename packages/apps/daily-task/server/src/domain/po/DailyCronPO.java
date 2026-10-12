package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * cron表达式(自定义的)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_cron")
@EqualsAndHashCode(callSuper = true)
public class DailyCronPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private String cron;
    private String description;
    /**
     * 任务类型
     */
    private String dailyType;
}
