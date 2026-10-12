package com.guhecloud.cube.biz.daily.task.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 记录对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_record_target")
@EqualsAndHashCode(callSuper = true)
public class DailyRecordTargetPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long taskId;
    private String taskName;
    private Long recordId;
    private String targetCategory;
    private Long targetId;
    private String targetName;
    private String targetCode;
    private String lntAndLat;
    private String location;
    private String executionResult;
}
