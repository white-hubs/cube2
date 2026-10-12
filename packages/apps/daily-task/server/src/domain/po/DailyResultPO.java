package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 执行结果项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_result")
@EqualsAndHashCode(callSuper = true)
public class DailyResultPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否启用
     */
    private Boolean enable;
    /**
     * 结果项 List<DailyResultLabelDTO>
     */
    private String resultLabelJson;
    /**
     * 任务类型
     */
    private String dailyType;
}
