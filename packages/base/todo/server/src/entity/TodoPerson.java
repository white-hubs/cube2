package com.guhecloud.cube.biz.todo.server.entity;

import com.guhecloud.cube.biz.todo.server.enumeration.TodoTaskStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.cube.biz.todo.server.filter.TodoPersonCustomDeptFieldFilter;
import com.guhecloud.cube.biz.todo.server.filter.TodoPersonUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
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
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "user_id", fieldFilter = TodoPersonUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "user_id", fieldParamConvert = TodoPersonCustomDeptFieldFilter.class)
}
)
public class TodoPerson extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 待办任务主键
     */
    private Long todoTaskId;

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
     * 待办状态
     */
    private TodoTaskStatusEnum status;
}
