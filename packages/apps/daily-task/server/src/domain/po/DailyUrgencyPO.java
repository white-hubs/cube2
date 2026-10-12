package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务紧急程度
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_urgency")
@EqualsAndHashCode(callSuper = true)
public class DailyUrgencyPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 紧急程度等级
     */
    private Integer level;
    /**
     * 紧急程度名称
     */
    private String name;
    /**
     * 任务类型
     */
    private String dailyType;
}
