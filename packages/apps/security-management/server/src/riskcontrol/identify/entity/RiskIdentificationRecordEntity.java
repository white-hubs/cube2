package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.entity;

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
@TableName("sm_risk_identification_record")
@DataPermission({
        @DataPermissionField(permission = "CURRENT_USER", field = "identifier_user_id"),
        @DataPermissionField(permission = "CURRENT_USER_DEPT", field = "identifier_user_id",
                             fieldFilter = RiskCurrentUserDeptFieldFilter.class),
        @DataPermissionField(permission = "CUSTOM_DEPT", field = "identifier_user_id",
                             fieldParamConvert = RiskCustomDeptFieldParamConvert.class)
})
public class RiskIdentificationRecordEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long objectId;
    private Long unitId;
    private String identifyRoundNo;
    private String operationType;
    private String identifierUserId;
    private LocalDateTime submitTime;
    private String snapshotJson;
}
