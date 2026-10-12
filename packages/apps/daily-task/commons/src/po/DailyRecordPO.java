package com.guhecloud.cube.biz.daily.task.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.commons.filter.DailyTaskUserCustomDeptFieldFilter;
import com.guhecloud.cube.biz.daily.task.commons.filter.DailyTaskUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_record")
@EqualsAndHashCode(callSuper = true)
@DataPermission(value = {
        @DataPermissionField(permission = "CURRENT_USER", field = "executor_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "executor_id", fieldFilter = DailyTaskUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "executor_id",
                             fieldParamConvert = DailyTaskUserCustomDeptFieldFilter.class
        )
}
)
public class DailyRecordPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 日常任务类型
     */
    private String dailyType;
    private Long taskId;
    private String taskName;
    /**
     * 执行时间
     */
    private LocalDateTime executionTime;
    /**
     * 执行人员id
     */
    private Long executorId;
    private String executorName;
    /**
     * 执行结果(正常异常)
     */
    private String executionResult;
    /**
     * 执行记录聚合状态
     */
    private String recordStatus;

    private String remark;
}
