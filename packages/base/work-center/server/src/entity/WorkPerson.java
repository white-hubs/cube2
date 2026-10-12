package com.guhecloud.cube.biz.work.center.server.entity;

import com.guhecloud.cube.biz.work.center.server.enumeration.WorkTaskStatusEnum;
import com.guhecloud.cube.biz.work.center.server.filter.WorkPersonCustomDeptFieldFilter;
import com.guhecloud.cube.biz.work.center.server.filter.WorkPersonUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DataPermission(value = {
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "user_id", fieldFilter = WorkPersonUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "user_id", fieldParamConvert = WorkPersonCustomDeptFieldFilter.class)
}
)
public class WorkPerson extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 工作任务主键
     */
    private Long workTaskId;

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 工作任务状态
     */
    private WorkTaskStatusEnum status;
}
