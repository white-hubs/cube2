package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task_feedback")
@EqualsAndHashCode(callSuper = true)
public class DailyTaskFeedbackPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 进度
     */
    private BigDecimal schedule;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件
     */
    private String files;
}
