package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分组设备
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupDeviceBindingLog extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long groupId;
    private Long deviceId;
}
