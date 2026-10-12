package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.common.filter.MaintainCurrentUserDeptFieldFilter;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 巡检修复对象人员关联关系
 * （谁应该去修复这个记录）
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("inspect_repair_relation")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "organization_id",
                             fieldFilter = MaintainCurrentUserDeptFieldFilter.class
        ),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "organization_id")
}
)
public class InspectRepairRelationPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 巡检记录ID
     */
    private Long id;
    /**
     * 组织机构ID
     */
    private Long organizationId;

    /**
     * 组织机构名
     */
    private String organizationName;

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 岗位名
     */
    private String positionName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户姓名
     */
    private String nickname;
}
