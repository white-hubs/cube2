package com.guhecloud.cube.biz.vmonitor.server.auth.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.vmonitor.server.auth.enumeration.ScopeTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("auth_scope")
public class AuthScopePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 范围类型
     */
    private ScopeTypeEnum scopeType;

    /**
     * 范围名称
     */
    private String scopeName;

    /**
     * 范围id
     */
    private Long scopeId;

    /**
     * 权限组id
     */
    private Long authGroupId;
}
