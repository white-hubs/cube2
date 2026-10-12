package com.guhecloud.cube.biz.vmonitor.server.auth.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("auth_group")
public class AuthGroupPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
}
