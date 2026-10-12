package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.unit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.filter.RiskCurrentUserDeptFieldFilter;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.filter.RiskCustomDeptFieldParamConvert;
import com.guhecloud.platform.database.annotation.DataPermission;
import com.guhecloud.platform.database.annotation.DataPermissionField;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sm_risk_unit")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "responsible_user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "responsible_user_id",
                             fieldFilter = RiskCurrentUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "responsible_user_id",
                             fieldParamConvert = RiskCustomDeptFieldParamConvert.class)
})
public class RiskUnitEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String unitCode;
    private String unitName;
    private Long areaId;
    private Long unitTypeId;
    private String responsibleUserId;
    private Integer identifyCycleDays;
    private String identifyStatus;
    private String currentIdentifyRoundNo;
    private LocalDateTime lastIdentifySubmitTime;
    private LocalDateTime lastIdentifyReviewTime;
    private LocalDateTime nextIdentifyTime;
    private String locationDesc;
    private String description;
    private String inherentRiskLevel;
    private String controlRiskLevel;
    private String riskLevel;
    private String controlStatus;
    private String gisPoint;
}
