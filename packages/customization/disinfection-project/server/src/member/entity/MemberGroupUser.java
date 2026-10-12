package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分组人员
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupUser extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long userId;
    private Long groupId;
    // 当前选择的分组
    private Boolean selectGroup;
    // 角色 MemberGroupRoleEnum
    private String role;
}
