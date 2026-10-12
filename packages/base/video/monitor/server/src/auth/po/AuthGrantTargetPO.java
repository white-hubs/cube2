package com.guhecloud.cube.biz.vmonitor.server.auth.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.vmonitor.server.auth.enumeration.GrantTargetEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("auth_grant_target")
public class AuthGrantTargetPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 对象类型
     */
    private GrantTargetEnum targetType;

    /**
     * 对象id
     */
    private Long targetId;

    /**
     * 权限组id
     */
    private Long authGroupId;
}
