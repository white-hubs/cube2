package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 计划执行记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_job_record")
@EqualsAndHashCode(callSuper = true)
public class DailyJobRecordPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 本次执行的计划主键
     */
    private String planIds;
    /**
     * 执行状态
     * (1成功0失败)
     */
    private Boolean executionStat;
    /**
     * 异常信息
     */
    private String errorMessage;
}
