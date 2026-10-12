package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 执行标准
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_standard")
@EqualsAndHashCode(callSuper = true)
public class DailyStandardPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 标准名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 任务种类ID（多个ID用逗号分隔，如：1,2,3）
     */
    private String categoryIds;
    /**
     * 是否启用
     */
    private Boolean enable;
    /**
     * 任务类型
     */
    private String dailyType;
}
