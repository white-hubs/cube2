package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 任务的执行内容
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task_item")
@EqualsAndHashCode(callSuper = true)
public class DailyTaskItemPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long taskId;
    private Long itemId;
    private String itemName;
    private String itemContent;
    /**
     * 操作指引
     */
    private String operationGuide;
    /**
     * 执行结果对象(应该是个集合)
     */
    private String resultJsonString;
}
