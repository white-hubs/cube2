package com.guhecloud.cube.base.flow.context.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.base.flow.context.enumeration.PermissionType;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 14:52
 * 自定义表单权限
 */

@Data
@TableName("form_permission")
@EqualsAndHashCode(callSuper = false)
public class FormPermissionPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 表单流程ID
     */
    private Long formFlowId;

    /**
     * 权限id（角色ID、用户ID、部门ID）
     */
    private Long permissionId;

    /**
     * 权限类型（1：角色；2：用户；3：部门）
     */
    private PermissionType permissionType;

}
