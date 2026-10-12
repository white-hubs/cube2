package com.guhecloud.cube.srv.gen.appraisals.server.process.entity;

import com.guhecloud.cube.srv.gen.appraisals.server.process.filter.AppraisalCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 表单考核流程记录表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "organization_id",
                             fieldFilter = AppraisalCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "organization_id")
}
)
public class FormAppraisalProcess extends BasicEnhancedEntity<Long> {
    private Long id;
    /**
     * 流程实例 ID
     */
    private String processInstanceId;
    /**
     * 发起人 ID
     */
    private Long userId;
    /**
     * 部门 ID
     */
    private Long organizationId;
    /**
     * 岗位 IDS
     */
    private String postIds;
    /**
     * 角色 IDS
     */
    private String roleIds;
    /**
     * 职位 IDS
     */
    private String appointmentIds;
}
