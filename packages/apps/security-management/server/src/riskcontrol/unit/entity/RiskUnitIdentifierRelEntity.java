package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.filter.RiskCurrentUserDeptFieldFilter;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.filter.RiskCustomDeptFieldParamConvert;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_unit_identifier_rel")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "user_id",
                             fieldFilter = RiskCurrentUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "user_id",
                             fieldParamConvert = RiskCustomDeptFieldParamConvert.class)
})
public class RiskUnitIdentifierRelEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long unitId;
    private String userId;
}
