package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
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
@TableName("daily_standard_target_type_relation")
@EqualsAndHashCode(callSuper = true)
public class DailyStandardTargetTypeRelationPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long standardId;
    private TaskTargetTypeEnum type;
    private String targetTypeId;

}
