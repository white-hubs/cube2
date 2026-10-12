package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.commons.filter.DailyTaskUserCustomDeptFieldFilter;
import com.guhecloud.cube.biz.daily.task.commons.filter.DailyTaskUserDeptFieldFilter;
import com.guhecloud.cube.biz.daily.task.domain.po.base.DailyBaseSuperintendent;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 任务人员
 */
@Data
@SuperBuilder
@AllArgsConstructor
@TableName("daily_task_user")
@EqualsAndHashCode(callSuper = true)
@DataPermission(value = {
        @DataPermissionField(permission = "CURRENT_USER", field = "target_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "target_id", fieldFilter = DailyTaskUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "target_id", fieldParamConvert = DailyTaskUserCustomDeptFieldFilter.class)
}
)
public class DailyTaskUserPO extends DailyBaseSuperintendent {
    public DailyTaskUserPO() {}

    public DailyTaskUserPO(Long id, Long masterId, String superintendentType, Long targetId, String createBy,
                           LocalDateTime createTime, String updateBy, LocalDateTime updateTime, Integer tenant, Boolean deleted
                          ) {
        super(id, masterId, superintendentType, targetId, createBy, createTime, updateBy, updateTime, tenant, deleted);
    }

    private Long id;
    /**
     * 人员类型 DailyTaskUserTypeEnum
     * (负责人/协同人)
     */
    private String taskUserType;
}
