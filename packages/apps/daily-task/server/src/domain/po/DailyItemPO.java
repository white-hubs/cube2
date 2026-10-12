package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 执行项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_item")
@EqualsAndHashCode(callSuper = true)
public class DailyItemPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private String content;
    private String operationGuide;
    private String tag;
    /**
     * 适配对象类型（多个类型用逗号分隔，如：AREA,DEVICE,PROPERTY）
     */
    private String targetTypes;
    /**
     * 任务类型
     */
    private String dailyType;

    private Boolean temp;
}
